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
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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
        if (Objects.isNull(entityType) || Objects.isNull(id)) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }
        MetaData metaData = metaDataMap.get(entityType);
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", entityType.getSimpleName()));
        }
        if (!metaData.checkIdCompatibility(id.getClass())) {
            throw new OrmException("Wrong type of ID object.");
        }
        T entity = null;
        String sqlQuery = buildSelectSqlQueryWithWhereClause(entityType, metaData.getPkMetaField().getDbFieldName());

        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            preparedStatement.setObject(1, id);
            resultSet = preparedStatement.executeQuery();

            // check if it's one row
            if (resultSet.next()) {
                entity = buildEntity(entityType, resultSet).orElseGet(() -> null);
                if (!resultSet.isLast()) {
                    throw new OrmException("Entity at database has a few primary keys." +
                            "Use @EmbeddedId or change the database schema.");
                }
            } else {
                // didn't find an entity
                return null;
            }
            // if it's has zero rows then return null
        } catch (Exception e) {
            LOG.error("There was an exception {}, during select {} by {}", e, entityType.getSimpleName(), id);
            throw new OrmException(e);
        }

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
        Set<Class<?>> implicitDependencies = dependencyGraph.getAllDependencies(rootType);
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
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaDataMap.get(currentClass);
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        List<ExternalMetaField> externalMetaFields = metaData.getExternalMetaField();
        List<InternalMetaField> internalMetaFields = metaData.getInternalMetaField();
        String updateQuery = buildUpdateQuery(currentClass);
        try(PreparedStatement statement = connection.prepareStatement(updateQuery)){
            List<String> parameters = collectUpdateParameters(internalMetaFields,externalMetaFields,object);
            for (int i = 0; i < parameters.size(); i++){
                statement.setObject(i+1,parameters.get(i));
            }
            statement.executeQuery();
        } catch (Exception e){
            LOG.error("There was an exception {}, during update query for {} ", e, object.getClass().getSimpleName() );
            throw new OrmException(e);
        }
    }

    private String buildUpdateQuery(Class<?> currentClass) {
        MetaData classMetaData = metaDataMap.get(currentClass);
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
                .append(" SET (")
                .append(String.join(",", foreignKeyFieldNames))
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
        return internalMetaFields.stream()
                .map(InternalMetaField::getDbFieldName)
                .collect(Collectors.toList());
    }

    private List<String> collectUpdateParameters(List<InternalMetaField> internalMetaFields, List<ExternalMetaField> externalMetaFields, Object object) throws IllegalAccessException {
        List<String> parameters = new ArrayList<>();
        for (ExternalMetaField externalMetaField : externalMetaFields){
            Object externalObject = externalMetaField.getField().get(object);
            if(Objects.isNull(externalObject)){
                parameters.add("NULL");
            } else {
                MetaData externalClassMetaData = metaDataMap.get(externalObject.getClass());
                parameters.add(externalClassMetaData.getPrimaryKey().get(externalObject).toString());
            }
        }
        for (InternalMetaField internalMetaField : internalMetaFields) {
            if(Objects.isNull(internalMetaField)){
                parameters.add("NULL");
            } else {
                parameters.add(internalMetaField.getField().get(object).toString());
            }

        }
        return parameters;
    }

    @Override
    public void delete(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaDataMap.get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        StringBuilder query = new StringBuilder();
        query.append("DELETE FROM ")
                .append(metaDataMap.get(currentClass).getEntityDbName())
                .append(" WHERE ")
                .append(metaDataMap.get(currentClass).getPrimaryKey())
                .append(" = ")
                .append("?");
        try(PreparedStatement statement = connection.prepareStatement(query.toString())) {
            statement.setObject(1,metaDataMap.get(currentClass).getPrimaryKey().get(object));
            statement.executeQuery();
        } catch (SQLException | IllegalAccessException e) {
            LOG.error("There was an exception {}, during delete query for {} by {}", e, object.getClass().getSimpleName(), object.toString());
            throw new OrmException(e);
        }
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
    //redundant until implementation of cascade
    private void recursiveDelete(Stack<String> query, Set<Class<?>> traversedClasses, Object object) {
        Class<?> currentClass = object.getClass();
        List<ExternalMetaField> externalFields = metaDataMap.get(object.getClass()).getExternalMetaField();
        try {
            query.push("DELETE FROM " + metaDataMap.get(currentClass).getEntityDbName()
                    + " WHERE " + metaDataMap.get(currentClass).getPrimaryKey()
                    + " = " + metaDataMap.get(currentClass).getPrimaryKey().get(object));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        traversedClasses.add(currentClass);

        if (externalFields.size() != 0) {
            for (ExternalMetaField externalField : externalFields) {
                if (!traversedClasses.contains(externalField.getFieldType())) {
                    Object externalObject = null;
                    try {
                        externalObject = currentClass.getDeclaredField(externalField.getFieldName()).get(object);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    recursiveDelete(query, traversedClasses, externalObject);
                }
            }
        }
    }
}

