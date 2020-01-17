package com.softserve.easy.core;

import com.softserve.easy.annotation.Id;
import com.softserve.easy.annotation.ManyToOne;
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
//        String sqlQuery = "INSERT INTO users (users.id,users.login,users.password,users.email,users.country_code) " +
//                "VALUES (6,'Jon','Jon123123','Jon@gmail.com',100);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            //if we have id
            int i = 0;
            if(hasId(object)) {
                //get Value Internal-Fields
                for (Field f : metaData.getFields()) {
                    f.setAccessible(true);
                    i++;
                    preparedStatement.setObject(i, f.get(object));
                }
                //get Value External-Fields
                for (Field f : metaData.getFields()) {
                    if (f.isAnnotationPresent(ManyToOne.class)) {
                        i++;
                        Object object1 = f.get(object);
                        preparedStatement.setObject(i, getIdValue(object1));
                    }
                }
            } else {
                for (Field f : metaData.getFields()) {
                    f.setAccessible(true);
                    i++;
                    if(!f.isAnnotationPresent(Id.class)) {
                        preparedStatement.setObject(i, f.get(object));
                    }
                }
                //get Value External-Fields
                for (Field f : metaData.getFields()) {
                    if (f.isAnnotationPresent(ManyToOne.class)) {
                        i++;
                        Object object1 = f.get(object);
                        preparedStatement.setObject(i, getIdValue(object1));
                    }
                }
            }
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getLong(1);
                }
                else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
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
            sb.append(currentMetaData.getJoinedInternalFieldsNames());
        } else {
            sb.append(currentMetaData.getJoinedInternalFieldsNamesWithoutPrimaryKey());
        }

        if(currentMetaData.getCountExternalFields() > 0) {
            sb.append(",");
            sb.append(currentMetaData.getJoinedExternalFieldsNames());
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

    private Object getIdValue(Object o){
        Field[] fields = o.getClass().getDeclaredFields();
        for(Field f: fields) {
            f.setAccessible(true);
            if(f.isAnnotationPresent(Id.class)) {
                try {
                    return f.get(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private boolean hasId(Object o) {
        return getIdValue(o) != null;
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

//    public boolean hasObjectsId(Object o) {
//
//        return
//    }

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

