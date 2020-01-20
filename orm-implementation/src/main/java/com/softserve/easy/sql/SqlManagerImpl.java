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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SqlManagerImpl implements SqlManager {
    private static final Logger LOG = LoggerFactory.getLogger(SqlManagerImpl.class);
    private final MetaContext metaContext;

    public SqlManagerImpl(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    @Override
    public SelectQuery buildSelectByPkQuery(MetaData entityMetaData, Serializable id) {
        SelectQuery selectQuery = new SelectQuery();

        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        for (InternalMetaField metaField : internalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName());
        }

        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        for (ExternalMetaField metaField : externalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName());
            if (metaField.getEntityFetchType().equals(FetchType.EAGER)) {
                    addDependencyToQuery(selectQuery, metaField);
            }
        }

        selectQuery.addCondition(BinaryCondition.equalTo(entityMetaData.getPkMetaField().getInternalDbColumn(), id));
        selectQuery.validate();
        LOG.info("Built a select query for {} entity with id: {}.\n{}",
                entityMetaData.getEntityClass().getSimpleName(), id, selectQuery.toString());
        return selectQuery;
    }

    private void addDependencyToQuery(SelectQuery selectQuery, ExternalMetaField metaField) {
        MetaData parentMetaData = metaField.getMetaData();
        MetaData childMetaData = metaContext.getMetaDataMap().get(metaField.getFieldType());

        List<InternalMetaField> internalMetaFields = childMetaData.getInternalMetaField();
        for (InternalMetaField internalMetaField : internalMetaFields) {
            selectQuery.addAliasedColumn(internalMetaField.getInternalDbColumn(), internalMetaField.getDbFieldFullName());
        }

        List<ExternalMetaField> externalMetaFields = childMetaData.getExternalMetaField();
        for (ExternalMetaField externalMetaField : externalMetaFields) {
            selectQuery.addAliasedColumn(externalMetaField.getExternalDbColumn(), externalMetaField.getForeignKeyFieldFullName());
        }

        DbJoin dbJoin = new DbJoin(parentMetaData.getDbTable().getSpec(),
                parentMetaData.getDbTable(),
                childMetaData.getDbTable(),
                new DbColumn[]{metaField.getExternalDbColumn()},
                new DbColumn[]{childMetaData.getPkMetaField().getInternalDbColumn()});

        selectQuery.addJoins(SelectQuery.JoinType.INNER, dbJoin);
    }

    @Override
    public SelectQuery buildSelectAllQuery(MetaData entityMetaData) {
        SelectQuery selectQuery = new SelectQuery();
        LOG.info("Built a select all query for {} entity.\n{}"
                , entityMetaData.getEntityClass().getSimpleName(), selectQuery.toString());
        return selectQuery;
    }

    @Override
    public UpdateQuery buildUpdateByPkQuery(MetaData entityMetaData, Object object) {
        UpdateQuery updateQuery = new UpdateQuery(entityMetaData.getDbTable());
        InternalMetaField pkMetaField = entityMetaData.getPkMetaField();
        Field pkField = pkMetaField.getField();
        boolean previous = pkField.isAccessible();
        Object pkValue = null;
        Map<DbColumn, Object> dbColumnObjectMap = null;
        pkField.setAccessible(true);
        try {
            pkValue = pkField.get(object);
            dbColumnObjectMap = collectUpdateParameters(
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

    private Map<DbColumn, Object> collectUpdateParameters(List<InternalMetaField> internalMetaFields,
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
                    throw new OrmException("Updated entity has a wrong state. The object of the external field's should has" +
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
        InternalMetaField pkMetaField = entityMetaData.getPkMetaField();
        Field pkField = pkMetaField.getField();
        boolean accessible = pkField.isAccessible();
        Object pkValue = null;
        if (!accessible)
            pkField.setAccessible(true);
        try {
            pkValue = pkField.get(object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to PK field: {}", pkField.getName());
        }
        // return value back
        if (!accessible) {
            pkField.setAccessible(false);
        }
        if (Objects.isNull(pkValue)) {
            throw new OrmException("Couldn't build delete query. Pk field doesn't have value.");
        }

        deleteQuery.addCondition(BinaryCondition.equalTo(pkMetaField.getInternalDbColumn(), pkValue));
        deleteQuery.validate();
        return deleteQuery;
    }

    @Override
    public InsertQuery buildInsertQueryWithId(MetaData entityMetaData, Object object, Serializable id) {
        return null;
    }

    @Override
    public InsertQuery buildInsertQuery(MetaData entityMetaData, Object object) {
        return null;
    }

}
