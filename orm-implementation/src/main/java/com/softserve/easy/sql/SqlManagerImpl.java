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
import java.lang.reflect.Field;
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
        UpdateQuery updateQuery = new UpdateQuery(entityMetaData.getDbTable());
        InternalMetaField pkMetaField = entityMetaData.getMetaPrimaryKey();
        Field pkField = pkMetaField.getField();
        boolean previous = pkField.isAccessible();
        Object pkValue = null;
        Map<DbColumn, Object> dbColumnObjectMap = null;
        pkField.setAccessible(true);
        try {
            pkValue = pkField.get(object);
            dbColumnObjectMap = collectParameters(
                    entityMetaData.getInternalMetaFieldWithoutPk(),
                    entityMetaData.getExternalMetaField(), object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to field: {}", e.getMessage());
        }
        pkField.setAccessible(previous);
        if (Objects.isNull(pkValue)) {
            throw new OrmException("Couldn't build update query. Pk field doesn't have value.");
        }

        Objects.requireNonNull(dbColumnObjectMap).forEach(updateQuery::addSetClause);
        updateQuery.addCondition(BinaryCondition.equalTo(pkMetaField.getInternalDbColumn(), pkValue));
        updateQuery.validate();
        return updateQuery;
    }

    private Map<DbColumn, Object> collectParameters(List<InternalMetaField> internalMetaFields,
                                                    List<ExternalMetaField> externalMetaFields,
                                                    Object object)
            throws IllegalAccessException {
        Map<DbColumn, Object> dbColumnObjectMap = new LinkedHashMap<>();
        for (ExternalMetaField externalMetaField : externalMetaFields) {
            checkAndProvideAccessibility(externalMetaField.getField());
            Object value = externalMetaField.getField().get(object);
            if (Objects.isNull(value)) {
                dbColumnObjectMap.put(externalMetaField.getExternalDbColumn(), null);
            } else {
                MetaData valueEntityMetaData = metaContext.getMetaDataMap().get(value.getClass());
                checkAndProvideAccessibility(valueEntityMetaData.getPrimaryKey());
                Object externalObjectsPkValue = valueEntityMetaData.getPrimaryKey().get(value);
                if (Objects.isNull(externalObjectsPkValue)) {
                    LOG.warn("The field {} of the external entity {} should be defined.",
                            valueEntityMetaData.getPrimaryKey(), valueEntityMetaData.getClass().getSimpleName());
                    throw new OrmException("Entity has a wrong state. The object of the external field's should has" +
                            "a defined primary key field.");
                }
                dbColumnObjectMap.put(externalMetaField.getExternalDbColumn(), externalObjectsPkValue);
            }
        }
        for (InternalMetaField internalMetaField : internalMetaFields) {
            checkAndProvideAccessibility(internalMetaField.getField());
            Object internalObject = internalMetaField.getField().get(object);
            if (Objects.isNull(internalObject)) {
                dbColumnObjectMap.put(internalMetaField.getInternalDbColumn(), null);
            } else {
                dbColumnObjectMap.put(internalMetaField.getInternalDbColumn(),
                        internalMetaField.getField().get(object));
            }

        }
        return dbColumnObjectMap;
    }

    private void checkAndProvideAccessibility(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    @Override
    public DeleteQuery buildDeleteByPkQuery(MetaData entityMetaData, Object object) {
        DeleteQuery deleteQuery = new DeleteQuery(entityMetaData.getDbTable());
        InternalMetaField pkMetaField = entityMetaData.getMetaPrimaryKey();
        Field pkField = pkMetaField.getField();
        boolean accessible = pkField.isAccessible();
        pkField.setAccessible(true);
        Object pkValue = null;
        try {
            pkValue = pkField.get(object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to PK field: {}", pkField.getName());
        }
        pkField.setAccessible(accessible);
        if (Objects.isNull(pkValue)) {
            throw new OrmException("Couldn't build delete query. Pk field doesn't have value.");
        }

        deleteQuery.addCondition(BinaryCondition.equalTo(pkMetaField.getInternalDbColumn(), pkValue));
        deleteQuery.validate();
        return deleteQuery;
    }

    @Override
    public InsertQuery buildInsertQueryWithPk(MetaData entityMetaData, Object object, Serializable id) {
        InsertQuery insertQuery = buildInsertQuery(entityMetaData, object);
        insertQuery.addColumn(entityMetaData.getMetaPrimaryKey().getInternalDbColumn(), id);
        insertQuery.validate();
        return insertQuery;
    }

    @Override
    public InsertQuery buildInsertQuery(MetaData entityMetaData, Object object) {
        InsertQuery insertQuery = new InsertQuery(entityMetaData.getDbTable());
        Map<DbColumn, Object> dbColumnObjectMap = null;
        try {
            dbColumnObjectMap = collectParameters(
                    entityMetaData.getInternalMetaFieldWithoutPk(),
                    entityMetaData.getExternalMetaField(), object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to field: {}", e.getMessage());
        }
        Objects.requireNonNull(dbColumnObjectMap).forEach(insertQuery::addColumn);
        insertQuery.validate();
        return insertQuery;
    }

}
