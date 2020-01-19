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
import java.util.List;

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
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        String sqlQuery = sqlManager.buildSelectByPkQuery(entityMetaData, id).toString();
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

        String updateQuery = sqlManager.buildUpdateByPkQuery(metaData, object).toString();
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            int rows = statement.executeUpdate();
            connection.commit();
            LOG.info("-------ROWS AFFECTED " + rows + "-------");
        } catch (Exception e) {
            LOG.error("There was an exception {}, during update query for {} ", e, object.getClass().getSimpleName());
            throw new OrmException(e);
        }
    }


    @Override
    public void deleteEntity(Object object) {
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        Field pkField = metaData.getPrimaryKey();
        String query = sqlManager.buildDeleteByPkQuery(metaData, object).toString();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.execute();
        } catch (SQLException  e) {
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
