package com.softserve.easy.jdbc;

import com.softserve.easy.action.ActionQueue;
import com.softserve.easy.bind.EntityBinder;
import com.softserve.easy.bind.EntityBinderImpl;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import com.softserve.easy.sql.SqlManager;
import com.softserve.easy.sql.SqlManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JDBCPersister implements Persister {
    private static final Logger LOG = LoggerFactory.getLogger(JDBCPersister.class);
    private ActionQueue actionQueue;

    private final Connection connection;
    private final MetaContext metaContext;
    private final SqlManager sqlManager;
    private final EntityBinder entityBinder;

    public JDBCPersister(Connection connection, MetaContext metaContext) {
        this.connection = connection;
        this.metaContext = metaContext;
        this.sqlManager = new SqlManagerImpl(metaContext);
        this.entityBinder = new EntityBinderImpl(metaContext);
    }


    @Override
    public <T> T getEntityById(Class<T> entityType, Serializable id) {
        LOG.info("Try to load entity {} by id {} from database.", entityType.getSimpleName(), id);
        String sqlQuery = sqlManager.buildSelectByIdSqlQuery(entityType,id).toString();
        T entity = null;
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            resultSet = preparedStatement.executeQuery();

            // check if it's one row
            if (resultSet.next()) {
                entity = entityBinder.buildEntity(entityType, resultSet).orElseGet(() -> null);
                if (!resultSet.isLast()) {
                    throw new OrmException("Entity at database has a few primary keys." +
                            "Use @EmbeddedId or change the database schema.");
                }
            } else {
                LOG.info("Entity {} by id {} doesn't exist.", entityType.getSimpleName(), id);
                return null;
            }
        } catch (Exception e) {
            LOG.error("There was an exception {}, during select {} by {}", e, entityType.getSimpleName(), id);
            throw new OrmException(e);
        }

        return entity;
    }

    @Override
    public void updateEntity(Object object) {
        Class<?> currentClass = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(currentClass);
        List<ExternalMetaField> externalMetaFields = metaData.getExternalMetaField();
        List<InternalMetaField> internalMetaFields = metaData.getInternalMetaField();
        internalMetaFields.remove(metaData.getPkMetaField());

        String updateQuery = sqlManager.buildUpdateQuery(currentClass).toString();
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
            throw new OrmException(e);
        }
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

    private void checkAndProvideAccessibility(Field field) {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    @Override
    public void deleteEntity(Object object) {
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        Field pkField = metaData.getPrimaryKey();
        checkAndProvideAccessibility(pkField);
        String query = sqlManager.buildDeleteQuery(object.getClass()).toString();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, pkField.get(object).toString());
            statement.execute();
        } catch (SQLException | IllegalAccessException e) {
            LOG.error("There was an exception {}, during delete query for {} by {}", e, object.getClass().getSimpleName(), object.toString());
            throw new OrmException(e);
        }
    }

    @Override
    public void insertEntity(Object object) {

    }
    @Override
    public void insertEntityWithId(Object object, Serializable id) {

    }
}
