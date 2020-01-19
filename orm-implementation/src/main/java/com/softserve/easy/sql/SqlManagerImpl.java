package com.softserve.easy.sql;

import com.healthmarketscience.sqlbuilder.*;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbJoin;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SqlManagerImpl implements SqlManager {
    private static final Logger LOG = LoggerFactory.getLogger(SqlManagerImpl.class);
    private final MetaContext metaContext;

    public SqlManagerImpl(MetaContext metaContext) {
        this.metaContext = metaContext;
    }

    @Override
    public SelectQuery buildSelectByIdSqlQuery(Class<?> entity, Serializable id) {
        SelectQuery selectQuery = new SelectQuery();
        MetaData rootMetaData = metaContext.getMetaDataMap().get(entity);

        List<InternalMetaField> internalMetaFields = rootMetaData.getInternalMetaField();
        for (InternalMetaField metaField : internalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getInternalDbColumn(), metaField.getDbFieldFullName());
            LOG.debug(metaField.getInternalDbColumn().getAbsoluteName());
            LOG.debug(metaField.getInternalDbColumn().getColumnNameSQL());
            LOG.debug(metaField.getInternalDbColumn().getName());

        }

        List<ExternalMetaField> externalMetaFields = rootMetaData.getExternalMetaField();
        for (ExternalMetaField metaField : externalMetaFields) {
            selectQuery.addAliasedColumn(metaField.getExternalDbColumn(), metaField.getForeignKeyFieldFullName());
            addDependencyToSqlQuery(selectQuery, metaField);
        }

        selectQuery.addCondition(BinaryCondition.equalTo(rootMetaData.getPkMetaField().getInternalDbColumn(), id));
        LOG.info("Built a select query for {} entity with id: {}.\n{}",entity.getSimpleName(), id, selectQuery.toString());
        return selectQuery;
    }

    private void addDependencyToSqlQuery(SelectQuery selectQuery, ExternalMetaField metaField) {
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
    public SelectQuery buildSelectAllSqlQuery(Class<?> entity) {
        SelectQuery selectQuery = new SelectQuery();
        LOG.info("Built a select all query for {} entity.\n{}",entity.getSimpleName(), selectQuery.toString());
        return selectQuery;
    }

    @Override
    public UpdateQuery buildUpdateQuery(Class<?> entity) {
        return null;
    }

    @Override
    public DeleteQuery buildDeleteQuery(Class<?> entity) {
        return null;
    }

    @Override
    public InsertQuery buildInsertQueryWithId(Class<?> entity, Serializable id) {
        return null;
    }

    @Override
    public InsertQuery buildInsertQuery(Class<?> entity) {
        return null;
    }

    public String buildDeleteSqlQuery(Object object) {
        MetaData classMetaData = metaContext.getMetaDataMap().get(object.getClass());
        List<String> foreignKeyFieldNames = getForeignKeyFieldNames(classMetaData);
        List<String> internalFieldNames = getInternalFieldNames(classMetaData);
        foreignKeyFieldNames = foreignKeyFieldNames.stream()
                .map(foreignKeyFieldName -> foreignKeyFieldName + " = ?")
                .collect(Collectors.toList());
        internalFieldNames = internalFieldNames.stream()
                .map(internalFieldName -> internalFieldName + " = ?")
                .collect(Collectors.toList());
        StringBuilder query = new StringBuilder()
                .append("UPDATE ")
                .append(classMetaData.getEntityDbName())
                .append(" SET ")
                .append(String.join(",", foreignKeyFieldNames))
                .append(", ")
                .append(String.join(",", internalFieldNames))
                .append(" WHERE ")
                .append(classMetaData.getPkMetaField().getDbFieldName())
                .append(" = ? ;");
        return query.toString();
    }

    private List<String> getForeignKeyFieldNames(MetaData classMetaData) {
        List<ExternalMetaField> externalMetaFields = classMetaData.getExternalMetaField();
        return externalMetaFields.stream()
                .map(ExternalMetaField::getForeignKeyFieldName)
                .collect(Collectors.toList());
    }

    private List<String> getInternalFieldNames(MetaData classMetaData) {
        List<InternalMetaField> internalMetaFields = classMetaData.getInternalMetaField();
        internalMetaFields.remove(classMetaData.getPkMetaField());
        return internalMetaFields.stream()
                .map(InternalMetaField::getDbFieldName)
                .collect(Collectors.toList());
    }

    private String buildDeleteQuery(MetaData metaData) {
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ")
                .append(metaContext.getMetaDataMap().get(metaData.getEntityClass()).getEntityDbName())
                .append(" WHERE ")
                .append(metaData.getPkMetaField().getDbFieldFullName())
                .append(" = ")
                .append("?");
        return query.toString();
    }

    public String buildDeleteSqlQuery2(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        Field pkField = metaData.getPrimaryKey();
        if (!pkField.isAccessible()) {
            pkField.setAccessible(true);
        }
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ")
                .append(metaContext.getMetaDataMap().get(metaData.getEntityClass()).getEntityDbName())
                .append(" WHERE ")
                .append(metaData.getPkMetaField().getDbFieldFullName())
                .append(" = ")
                .append("?");
        return query.toString();
    }

    public String buildUpdateQuery(Object object) {
        return null;
    }
}
