package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class SessionImpl implements Session {
    private static final Logger LOG = LoggerFactory.getLogger(SessionImpl.class);

    private Connection connection;
    private Map<Class<?>, MetaData> metaDataMap;
    private DependencyGraph dependencyGraph;
    private Transaction transaction;

    public SessionImpl(Connection connection, Map<Class<?>, MetaData> metaDataMap, DependencyGraph dependencyGraph) {
        this.connection = connection;
        this.metaDataMap = metaDataMap;
        this.dependencyGraph = dependencyGraph;
    }

    @Override
    public Serializable save(Object object) {
        return null;
    }

    @Override
    public <T> T get(Class<T> entityType, Serializable id) {
        MetaData metaData = metaDataMap.get(entityType);
        T entity = null;
        if (metaData.checkTypeCompatibility(entityType)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", entityType.getSimpleName()));
        }
        if (metaData.checkIdCompatibility(id.getClass())) {
            throw new OrmException("Wrong type of ID object.");
        }
        String sqlQuery = buildSelectSqlQueryWithWhereClause(entityType, metaData.getPkMetaField().getDbFieldName());

        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setObject(1, id);
            resultSet = preparedStatement.executeQuery();

            System.out.println(resultSet.getRow());
            // check if it's one row
            if (resultSet.next()) {
                entity = buildEntity(entityType, resultSet).orElseGet(() -> null);
            } else {
                throw new IllegalArgumentException("The result set must have exactly one row.");
            }
            // if it's has zero rows then return null
        } catch (Exception e) {
            LOG.error("There was an exception {}, during select {} by {}", e, entityType.getSimpleName(), id);
            throw new OrmException(e);
        }

        // TODO: process 'Optional' properly

        return entity;
    }

    public <T> Optional<T> buildEntity(Class<T> entityType, ResultSet resultSet) throws Exception {
        MetaData entityMetaData = metaDataMap.get(entityType);
        List<InternalMetaField> internalMetaFields = entityMetaData.getInternalMetaField();
        List<ExternalMetaField> externalMetaFields = entityMetaData.getExternalMetaField();
        // TODO: implement PROXY

        final T instance = entityType.newInstance();
        for (InternalMetaField metaField : internalMetaFields) {
            Field field = metaField.getField();
            boolean accessible = field.isAccessible();
            if (!accessible)
                field.setAccessible(true);
            field.set(instance, resultSet.getObject(metaField.getDbFieldName(), metaField.getFieldType()));
            // return value back
            if (!accessible) {
                field.setAccessible(false);
            }
        }

        for (ExternalMetaField metaField : externalMetaFields) {
            Field field = metaField.getField();
            Object childInstance = buildEntity(metaField.getFieldType(), resultSet).orElseGet(null);
            boolean accessible = field.isAccessible();
            if (!accessible)
                field.setAccessible(true);
            field.set(instance, childInstance);
            // return value back
            if (!accessible)
                field.setAccessible(false);
        }
        return Optional.ofNullable(instance);
    }

    public String buildSelectSqlQuery(Class<?> rootType) {
        return buildSelectSqlQueryWithWhereClause(rootType, null);
    }

    // it could be expanded
    public String buildSelectSqlQueryWithWhereClause(Class<?> rootType, String fieldName) {
        MetaData rootMetaData = metaDataMap.get(rootType);
        Set<Class<?>> implicitDependencies = dependencyGraph.getImplicitDependencies(rootType);
        List<ExternalMetaField> externalMetaField = rootMetaData.getExternalMetaField();
        StringBuilder stringBuilder = new StringBuilder();

        // #SELECT CLAUSE
        stringBuilder.append(" SELECT ")
                .append(rootMetaData.getJoinedInternalFieldsNames());
        implicitDependencies.forEach(classDependency ->
                stringBuilder.append(",").append(metaDataMap.get(classDependency).getJoinedInternalFieldsNames())
        );
        // #/SELECT CLAUSE

        // #FROM CLAUSE
        stringBuilder.append(" FROM ")
                .append(rootMetaData.getEntityDbName());
        // #/FROM CLAUSE

        // #JOIN CLAUSE
        externalMetaField.forEach(exField -> {
                    MetaData childMetaData = metaDataMap.get(exField.getFieldType());
                    stringBuilder.append(getLeftJoinStatement(
                            childMetaData.getEntityDbName(),
                            exField.getForeignKeyFieldFullName(),
                            childMetaData.getPkMetaField().getDbFieldFullName()
                    ));
                }
        );
        // #/JOIN CLAUSE

        // #WHERE CLAUSE
        if (Objects.nonNull(fieldName)) {
            stringBuilder.append(" WHERE ")
                    // TODO: it doesn't work with child's entity fields
                    // TODO: need to refactor the AbstractMetaField classes
                    .append(rootMetaData.getEntityDbName())
                    .append(".")
                    .append(fieldName)
                    .append(" = ")
                    .append(" ? ");
        }
        // #/WHERE CLAUSE

        // #OTHER CLAUSE
        // #/OTHER CLAUSE

        // #END CLAUSE
        stringBuilder.append(";");
        // #END CLAUSE
        return stringBuilder.toString();
    }

    private String getLeftJoinStatement(String childTableName, String parentFkFieldName, String childPkName) {
        StringBuilder stringBuilder = new StringBuilder()
                .append(" LEFT JOIN ")
                .append(childTableName)
                .append(" ON ")
                .append(parentFkFieldName)
                .append(" = ")
                .append(childPkName);
        return stringBuilder.toString();
    }

    @Override
    public void update(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Object object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transaction beginTransaction() {
        throw new UnsupportedOperationException();
    }
}
