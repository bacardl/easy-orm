package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


public class SessionImpl implements Session {
    private static final Logger LOG = LoggerFactory.getLogger(SessionImpl.class);

    private final Connection connection;
    private final MetaContext metaContext;

    private Transaction transaction;

    public SessionImpl(Connection connection, MetaContext metaContext) {
        this.connection = connection;
        this.metaContext = metaContext;
    }

    @Override
    public Serializable save(Object object) {
        // checks Object
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }
        MetaData metaData = metaDataMap.get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }

        Object generatedId = null;
        String sqlQuery = buildInsertSqlQuery(object);
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            int i = 0;
            if(hasId(object)) {
                for (InternalMetaField f : metaData.getInternalMetaField()) {
                    f.getField().setAccessible(true);
                    i++;
                    preparedStatement.setObject(i, f.getField().get(object));
                }
            } else {
                for (InternalMetaField f : metaData.getInternalMetaFieldsWithoutPk()) {
                    f.getField().setAccessible(true);
                    i++;
                    preparedStatement.setObject(i, f.getField().get(object));
                }
            }

            for (ExternalMetaField f : metaData.getExternalMetaField()) {
                f.getField().setAccessible(true);
                i++;
                preparedStatement.setObject(i, getIdValue(f.getField().get(object)));
            }
            generatedId = preparedStatement.executeUpdate();

            if (generatedId == null) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }catch(Exception e) {
                    e.printStackTrace();
            }

        } catch (Exception e) {
            LOG.error("There was an exception {}, during insert {}", e, object.getClass().getSimpleName());
            throw new OrmException(e);
        }
        return (Serializable) generatedId;
    }

    public String buildInsertSqlQuery(Object object) {
        MetaData currentMetaData = metaDataMap.get(object.getClass());

        String tableName = currentMetaData.getEntityDbName();
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO ").append(tableName).append(" (");
        //have id or no
        if(hasId(object)) {
            sb.append(currentMetaData.getJoinedInternalFieldsNamesNotFull());
        } else {
            sb.append(currentMetaData.getJoinedInternalFieldsNamesNotFullWithoutPrimaryKey());
        }

        if(currentMetaData.getCountExternalFields() > 0) {
            sb.append(",");
            sb.append(currentMetaData.getJoinedExternalFieldsNamesNotFull());
        }

        sb.append(") ").append("VALUES (");
        long countInAndExFields = 0;
        //have id or no
        if(hasId(object)) {
            countInAndExFields = currentMetaData.getCountInternalFields() + currentMetaData.getCountExternalFields();
        } else {
            countInAndExFields = currentMetaData.getCountInternalFieldsWithoutPrimaryKey() + currentMetaData.getCountExternalFields();
        }

        for(int i = 0; i < countInAndExFields; i++){
            sb.append("?");
            if(i + 1 < countInAndExFields) {
                sb.append(",");
            }
        }
        sb.append(");");
        return sb.toString();
    }

    private Object getIdValue(Object object) throws IllegalAccessException {
        MetaData currentMetaData = metaDataMap.get(object.getClass());
        currentMetaData.getPrimaryKey().setAccessible(true);
        return  currentMetaData.getPrimaryKey().get(object);
    }

    private boolean hasId(Object o) {
        try {
            return getIdValue(o) != null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public <T> T get(Class<T> entityType, Serializable id) {
        if (Objects.isNull(entityType) || Objects.isNull(id)) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }
        MetaData metaData = metaContext.getMetaDataMap().get(entityType);
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
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
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
        MetaData rootMetaData = metaContext.getMetaDataMap().get(rootType);
        Set<Class<?>> implicitDependencies = metaContext.getDependencyGraph().getAllDependencies(rootType);
        List<ExternalMetaField> externalMetaField = rootMetaData.getExternalMetaField();
        StringBuilder stringBuilder = new StringBuilder();

        // #SELECT CLAUSE
        stringBuilder.append(" SELECT ")
                .append(rootMetaData.getJoinedInternalFieldsNames());
        implicitDependencies.forEach(classDependency ->
                stringBuilder.append(",").append(metaContext
                        .getMetaDataMap()
                        .get(classDependency)
                        .getJoinedInternalFieldsNames())
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
        MetaData metaData = metaContext.getMetaDataMap().get(currentClass);
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        List<ExternalMetaField> externalMetaFields = metaData.getExternalMetaField();
        List<InternalMetaField> internalMetaFields = metaData.getInternalMetaField();
        internalMetaFields.remove(metaData.getPkMetaField());
        String updateQuery = buildUpdateQuery(currentClass);
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            List<Object> parameters = collectUpdateParameters(internalMetaFields, externalMetaFields, object, metaData);
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            int rows = statement.executeUpdate();
            connection.commit();
            LOG.info("-------ROWS AFFECTED " + rows + "-------");
        } catch (Exception e) {
            LOG.error("There was an exception {}, during update query for {} ", e, object.getClass().getSimpleName());
        }
    }

    public String buildUpdateQuery(Class<?> currentClass) {
        MetaData classMetaData = metaContext.getMetaDataMap().get(currentClass);
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

    private List<Object> collectUpdateParameters(List<InternalMetaField> internalMetaFields, List<ExternalMetaField> externalMetaFields, Object object, MetaData metaData) throws IllegalAccessException {
        List<Object> parameters = new ArrayList<>();
        for (ExternalMetaField externalMetaField : externalMetaFields){
            checkAndProvideAccessibility(externalMetaField.getField());
            Object externalObject = externalMetaField.getField().get(object);
            if(Objects.isNull(externalObject)){
                parameters.add(null);
            } else {
                MetaData externalClassMetaData = metaContext.getMetaDataMap().get(externalObject.getClass());
                checkAndProvideAccessibility(externalClassMetaData.getPrimaryKey());
                parameters.add(externalClassMetaData.getPrimaryKey().get(externalObject));
            }
        }
        for (InternalMetaField internalMetaField : internalMetaFields) {
            checkAndProvideAccessibility(internalMetaField.getField());
            Object internalObject = internalMetaField.getField().get(object);
            if(Objects.isNull(internalObject)){
                parameters.add(null);
            } else {
                parameters.add(internalMetaField.getField().get(object));
            }

        }
        Field pkField = metaData.getPkMetaField().getField();
        checkAndProvideAccessibility(pkField);
        parameters.add(metaData.getPkMetaField().getField().get(object));
        return parameters;
    }

    @Override
    public void delete(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        Field pkField = metaData.getPrimaryKey();
        checkAndProvideAccessibility(pkField);
        String query = buildDeleteQuery(metaData);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1,pkField.get(object).toString());
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            LOG.error("There was an exception {}, during delete query for {} by {}", e, object.getClass().getSimpleName(), object.toString());
            throw new OrmException(e);
        }
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
    public String buildDeleteSqlQuery(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }
        Field pkField = metaData.getPrimaryKey();
        if(!pkField.isAccessible()){
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

    private void checkAndProvideAccessibility(Field field){
        if(!field.isAccessible()){
            field.setAccessible(true);
        }
    }

    //redundant
    private void recursiveDelete(Stack<String> query, Set<Class<?>> traversedClasses, Object object) {
        Class<?> currentClass = object.getClass();
        List<ExternalMetaField> externalFields = metaContext.getMetaDataMap().get(object.getClass()).getExternalMetaField();
        try {
            query.push("DELETE FROM " + metaContext.getMetaDataMap().get(currentClass).getEntityDbName()
                    + " WHERE " + metaContext.getMetaDataMap().get(currentClass).getPrimaryKey()
                    + " = " + metaContext.getMetaDataMap().get(currentClass).getPrimaryKey().get(object));
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

}
