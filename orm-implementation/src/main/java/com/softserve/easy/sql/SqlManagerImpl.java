package com.softserve.easy.sql;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbJoin;
import com.softserve.easy.constant.FetchType;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;
import com.softserve.easy.meta.primarykey.EmbeddedPrimaryKey;
import com.softserve.easy.meta.primarykey.SinglePrimaryKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class SqlManagerImpl implements SqlManager {
    private static final Logger LOG = LoggerFactory.getLogger(SqlManagerImpl.class);
    private final MetaContext metaContext;

    public SqlManagerImpl(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    @Override
    public SelectQuery buildSelectByPkQuery(MetaData entityMetaData, Serializable id) {
        final SelectQuery selectQuery = new SelectQuery();

        AbstractMetaPrimaryKey pk = entityMetaData.getPrimaryKey();
        handlePrimaryKey(pk, selectQuery, id);

        handleInternalMetaFields(
                metaField ->
                        selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName()),
                entityMetaData.getInternalMetaField()
        );

        handleExternalMetaFields(metaField -> {
                    selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName());
                    if (metaField.getEntityFetchType().equals(FetchType.EAGER)) {
                        addDependencyToQuery(selectQuery, metaField);
                    }
                },
                entityMetaData.getExternalMetaField()
        );

        selectQuery.validate();
        LOG.info("Built a select query for {} entity with id: {}.\n{}",
                entityMetaData.getEntityClass().getSimpleName(), id, selectQuery.toString());
        return selectQuery;
    }


    @Override
    public SelectQuery buildLazySelectByPkQuery(MetaData entityMetaData, Serializable id) {
        final SelectQuery selectQuery = new SelectQuery();

        AbstractMetaPrimaryKey pk = entityMetaData.getPrimaryKey();
        handlePrimaryKey(pk, selectQuery, id);

        handleInternalMetaFields(
                metaField ->
                        selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName()),
                entityMetaData.getInternalMetaField());

        handleExternalMetaFields(metaField ->
                        selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName()),
                entityMetaData.getExternalMetaField()
        );

        selectQuery.validate();
        LOG.info("Built a lazy select query for {} entity with id: {}.\n{}",
                entityMetaData.getEntityClass().getSimpleName(), id, selectQuery.toString());
        return selectQuery;
    }


    // this method does not support a composite fk yet
    private void addDependencyToQuery(SelectQuery selectQuery, ExternalMetaField externalMetaField) {
        MetaData parentMetaData = externalMetaField.getMetaData();
        MetaData childMetaData = metaContext.getMetaDataMap().get(externalMetaField.getFieldType());

        SinglePrimaryKey pk = (SinglePrimaryKey) childMetaData.getPrimaryKey();

        handleSinglePrimaryKey(
                singlePrimaryKey -> {
                    InternalMetaField pkMeta = singlePrimaryKey.getPrimaryKey();
                    selectQuery.addAliasedColumn(pkMeta.getInternalDbColumn(),
                            pkMeta.getDbFieldFullName());
                },
                pk
        );

        handleInternalMetaFields(
                metaField ->
                        selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName()),
                childMetaData.getInternalMetaField()
        );

        handleExternalMetaFields(metaField ->
                        selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName()),
                childMetaData.getExternalMetaField()
        );

        DbJoin dbJoin = new DbJoin(parentMetaData.getDbTable().getSpec(),
                parentMetaData.getDbTable(),
                childMetaData.getDbTable(),
                new DbColumn[]{externalMetaField.getExternalDbColumn()},
                new DbColumn[]{pk.getPrimaryKey().getInternalDbColumn()}
        );

        selectQuery.addJoins(SelectQuery.JoinType.INNER, dbJoin);
    }

    @Override
    public SelectQuery buildSelectAllQuery(MetaData entityMetaData) {
        final SelectQuery selectQuery = new SelectQuery();
        LOG.debug("Built a select all query for {} entity.\n{}"
                , entityMetaData.getEntityClass().getSimpleName(), selectQuery.toString());
        throw new UnsupportedOperationException();
    }

    @Override
    public UpdateQuery buildUpdateByPkQuery(MetaData entityMetaData, Object object) {
        final UpdateQuery updateQuery = new UpdateQuery(entityMetaData.getDbTable());
        AbstractMetaPrimaryKey primaryKey = entityMetaData.getPrimaryKey();

        Serializable primaryKeyValue = null;
        try {
            primaryKeyValue = primaryKey.retrieveValue(object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to field: {}", e.getMessage());
        }

        Map<DbColumn, Serializable> dbColumnObjectMap = collectParameters(
                entityMetaData.getInternalMetaField(),
                entityMetaData.getExternalMetaField(),
                object);

        if (Objects.isNull(primaryKeyValue)) {
            throw new OrmException("Couldn't build update query. Pk field doesn't have value.");
        }

        Objects.requireNonNull(dbColumnObjectMap).forEach(updateQuery::addSetClause);

        handlePrimaryKey(primaryKey, updateQuery, primaryKeyValue);
        updateQuery.validate();
        return updateQuery;
    }

    private Map<DbColumn, Serializable> collectParameters(List<InternalMetaField> internalMetaFields,
                                                          List<ExternalMetaField> externalMetaFields,
                                                          Object object) {
        Map<DbColumn, Serializable> dbColumnObjectMap = new LinkedHashMap<>();

        handleExternalMetaFields(metaField -> {
                    try {
                        Object value = metaField.retrieveValue(object);
                        if (Objects.isNull(value)) {
                            dbColumnObjectMap.put(metaField.getExternalDbColumn(), null);
                        } else {
                            MetaData valueEntityMetaData = metaContext.getMetaDataMap().get(value.getClass());
                            Serializable externalObjectsPkValue = valueEntityMetaData.getPrimaryKey().retrieveValue(value);
                            if (Objects.isNull(externalObjectsPkValue)) {
                                LOG.error("The field {} of the external entity {} should be defined.",
                                        valueEntityMetaData.getPrimaryKey(), valueEntityMetaData.getClass().getSimpleName());
                                throw new OrmException("Entity has a wrong state. The object of the external field's should has" +
                                        "a defined primary key field.");
                            }
                            dbColumnObjectMap.put(metaField.getExternalDbColumn(), externalObjectsPkValue);
                        }
                    } catch (IllegalAccessException e) {
                        LOG.error("Couldn't retrieve a value from the external object's field." +
                                        "Field: {}, external object {}", metaField.getFieldName(),
                                metaField.getFieldType().getSimpleName());
                        throw new OrmException("Couldn't handle an object's field.", e);
                    }
                },
                externalMetaFields
        );
        handleInternalMetaFields(metaField -> {
                    try {
                        Serializable internalObject = metaField.retrieveValue(object);
                        if (Objects.isNull(internalObject)) {
                            dbColumnObjectMap.put(metaField.getInternalDbColumn(), null);
                        } else {
                            dbColumnObjectMap.put(metaField.getInternalDbColumn(), internalObject);
                        }
                    } catch (IllegalAccessException e) {
                        LOG.error("Couldn't retrieve a value from the external object's field." +
                                        "Field: {}, external object {}", metaField.getFieldName(),
                                metaField.getFieldType().getSimpleName());
                        throw new OrmException("Couldn't handle an object's field.", e);
                    }
                },
                internalMetaFields
        );
        return dbColumnObjectMap;
    }

    @Override
    public DeleteQuery buildDeleteByPkQuery(MetaData entityMetaData, Object object) {
        final DeleteQuery deleteQuery = new DeleteQuery(entityMetaData.getDbTable());
        AbstractMetaPrimaryKey primaryKey = entityMetaData.getPrimaryKey();
        Serializable primaryKeyValue = null;
        try {
            primaryKeyValue = primaryKey.retrieveValue(object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to field: {}", e.getMessage());
        }

        if (Objects.isNull(primaryKeyValue)) {
            throw new OrmException("Couldn't build delete query. Pk field doesn't have value.");
        }
        handlePrimaryKey(primaryKey,deleteQuery, primaryKeyValue);

        deleteQuery.validate();
        return deleteQuery;
    }

    @Override
    public InsertQuery buildInsertQueryWithPk(MetaData entityMetaData, Object object, Serializable id) {
        InsertQuery insertQuery = buildInsertQuery(entityMetaData, object);
        handlePrimaryKey(entityMetaData.getPrimaryKey(), insertQuery, id);
        insertQuery.validate();
        return insertQuery;
    }

    @Override
    public InsertQuery buildInsertQuery(MetaData entityMetaData, Object object) {
        InsertQuery insertQuery = new InsertQuery(entityMetaData.getDbTable());
        Map<DbColumn, Serializable> dbColumnObjectMap = collectParameters(
                    entityMetaData.getInternalMetaField(),
                    entityMetaData.getExternalMetaField(),
                    object);

        Objects.requireNonNull(dbColumnObjectMap).forEach(insertQuery::addColumn);
        insertQuery.validate();
        return insertQuery;
    }

    private void handlePrimaryKey(AbstractMetaPrimaryKey pk, SelectQuery selectQuery, Serializable id) {
        switch (pk.getPrimaryKeyType()) {
            case SINGLE:
                handleSinglePrimaryKey(
                        singlePrimaryKey -> {
                            InternalMetaField pkMeta = singlePrimaryKey.getPrimaryKey();
                            selectQuery.addAliasedColumn(pkMeta.getInternalDbColumn(),
                                    pkMeta.getDbFieldFullName());
                            selectQuery.addCondition(BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), id));
                        },
                        (SinglePrimaryKey) pk
                );
                break;
            case COMPLEX:
                handleEmbeddedPrimaryKey(
                        embeddedPrimaryKey -> {
                            List<InternalMetaField> pksMeta = embeddedPrimaryKey.getPrimaryKeys();
                            handleInternalMetaFields(pkMeta -> {
                                        selectQuery.addAliasedColumn(pkMeta.getInternalDbColumn(),
                                                pkMeta.getDbFieldFullName());
                                        try {
                                            selectQuery.addCondition(
                                                    BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), pkMeta.retrieveValue(id))
                                            );
                                        } catch (IllegalAccessException e) {
                                            LOG.error("Couldn't retrieve a value from the embeddable object's field." +
                                                            "Field: {}, Embeddable object {}", pkMeta.getFieldName(),
                                                    embeddedPrimaryKey.getEmbeddableMetaData().getEmbeddableEntity().getSimpleName());
                                            throw new OrmException("Couldn't handle an id object.", e);
                                        }
                                    },
                                    pksMeta);
                        },
                        (EmbeddedPrimaryKey) pk
                );

        }
    }

    private void handlePrimaryKey(AbstractMetaPrimaryKey pk, UpdateQuery updateQuery, Serializable id) {
        switch (pk.getPrimaryKeyType()) {
            case SINGLE:
                handleSinglePrimaryKey(
                        singlePrimaryKey -> {
                            InternalMetaField pkMeta = singlePrimaryKey.getPrimaryKey();
                            updateQuery.addCondition(BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), id));
                        },
                        (SinglePrimaryKey) pk
                );
                break;
            case COMPLEX:
                handleEmbeddedPrimaryKey(
                        embeddedPrimaryKey -> {
                            List<InternalMetaField> pksMeta = embeddedPrimaryKey.getPrimaryKeys();
                            handleInternalMetaFields(pkMeta -> {
                                        try {
                                            updateQuery.addCondition(
                                                    BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), pkMeta.retrieveValue(id))
                                            );
                                        } catch (IllegalAccessException e) {
                                            LOG.error("Couldn't retrieve a value from the embeddable object's field." +
                                                            "Field: {}, Embeddable object {}", pkMeta.getFieldName(),
                                                    embeddedPrimaryKey.getEmbeddableMetaData().getEmbeddableEntity().getSimpleName());
                                            throw new OrmException("Couldn't handle an id object.");
                                        }
                                    },
                                    pksMeta);
                        },
                        (EmbeddedPrimaryKey) pk
                );

        }
    }

    private void handlePrimaryKey(AbstractMetaPrimaryKey pk, DeleteQuery deleteQuery, Serializable id) {
        switch (pk.getPrimaryKeyType()) {
            case SINGLE:
                handleSinglePrimaryKey(
                        singlePrimaryKey -> {
                            InternalMetaField pkMeta = singlePrimaryKey.getPrimaryKey();
                            deleteQuery.addCondition(BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), id));
                        },
                        (SinglePrimaryKey) pk
                );
                break;
            case COMPLEX:
                handleEmbeddedPrimaryKey(
                        embeddedPrimaryKey -> {
                            List<InternalMetaField> pksMeta = embeddedPrimaryKey.getPrimaryKeys();
                            handleInternalMetaFields(pkMeta -> {
                                        try {
                                            deleteQuery.addCondition(
                                                    BinaryCondition.equalTo(pkMeta.getInternalDbColumn(), pkMeta.retrieveValue(id))
                                            );
                                        } catch (IllegalAccessException e) {
                                            LOG.error("Couldn't retrieve a value from the embeddable object's field." +
                                                            "Field: {}, Embeddable object {}", pkMeta.getFieldName(),
                                                    embeddedPrimaryKey.getEmbeddableMetaData().getEmbeddableEntity().getSimpleName());
                                            throw new OrmException("Couldn't handle an id object.");
                                        }
                                    },
                                    pksMeta);
                        },
                        (EmbeddedPrimaryKey) pk
                );

        }
    }

    private void handlePrimaryKey(AbstractMetaPrimaryKey pk, InsertQuery insertQuery, Serializable id) {
        switch (pk.getPrimaryKeyType()) {
            case SINGLE:
                handleSinglePrimaryKey(
                        singlePrimaryKey -> {
                            InternalMetaField pkMeta = singlePrimaryKey.getPrimaryKey();
                            insertQuery.addColumn(pkMeta.getInternalDbColumn(), id);
                        },
                        (SinglePrimaryKey) pk
                );
                break;
            case COMPLEX:
                handleEmbeddedPrimaryKey(
                        embeddedPrimaryKey -> {
                            List<InternalMetaField> pksMeta = embeddedPrimaryKey.getPrimaryKeys();
                            handleInternalMetaFields(pkMeta -> {
                                        try {
                                            insertQuery.addColumn(pkMeta.getInternalDbColumn(), pkMeta.retrieveValue(id));
                                        } catch (IllegalAccessException e) {
                                            LOG.error("Couldn't retrieve a value from the embeddable object's field." +
                                                            "Field: {}, Embeddable object {}", pkMeta.getFieldName(),
                                                    embeddedPrimaryKey.getEmbeddableMetaData().getEmbeddableEntity().getSimpleName());
                                            throw new OrmException("Couldn't handle an id object.");
                                        }
                                    },
                                    pksMeta);
                        },
                        (EmbeddedPrimaryKey) pk
                );

        }
    }

    private void handleSinglePrimaryKey(Consumer<SinglePrimaryKey> consumer, SinglePrimaryKey singlePrimaryKey) {
        consumer.accept(singlePrimaryKey);
    }

    private void handleEmbeddedPrimaryKey(Consumer<EmbeddedPrimaryKey> consumer, EmbeddedPrimaryKey embeddedPrimaryKey) {
        consumer.accept(embeddedPrimaryKey);
    }

    private void handleInternalMetaFields(Consumer<InternalMetaField> consumer, List<InternalMetaField> internalMetaFields) {
        for (InternalMetaField metaField : internalMetaFields) {
            consumer.accept(metaField);
        }
    }

    private void handleExternalMetaFields(Consumer<ExternalMetaField> consumer, List<ExternalMetaField> externalMetaFields) {
        for (ExternalMetaField metaField : externalMetaFields) {
            consumer.accept(metaField);
        }
    }

}
