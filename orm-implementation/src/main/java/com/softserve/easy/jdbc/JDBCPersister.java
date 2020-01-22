package com.softserve.easy.jdbc;

import com.healthmarketscience.sqlbuilder.InsertQuery;
import com.softserve.easy.action.ActionQueue;
import com.softserve.easy.bind.EntityBinder;
import com.softserve.easy.bind.EntityBinderImpl;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;
import com.softserve.easy.sql.SqlManager;
import com.softserve.easy.sql.SqlManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.*;
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
        this.entityBinder = new EntityBinderImpl(metaContext, this);
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
    public <T> T getLazyEntityById(Class<T> entityType, Serializable id) {
        LOG.info("Try to load lazy entity {} by id {} from database.", entityType.getSimpleName(), id);
        MetaData entityMetaData = metaContext.getMetaDataMap().get(entityType);
        String sqlQuery = sqlManager.buildSelectByPkQuery(entityMetaData, id).toString();
        T entity = null;
        ResultSet resultSet = null;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            resultSet = preparedStatement.executeQuery();

            // check if it's one row
            if (resultSet.next()) {
                entity = entityBinder.buildLazyEntity(entityType, resultSet);
                if (!resultSet.isLast()) {
                    throw new OrmException("Entity at database has a few primary keys." +
                            "Use @EmbeddedId or change the database schema.");
                }
            } else {
                LOG.info("Lazy Entity {} by id {} doesn't exist.", entityType.getSimpleName(), id);
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
        Class<?> entityType = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(entityType);
        LOG.info("Try to update entity {} from database.", entityType.getSimpleName());
        String updateQuery = sqlManager.buildUpdateByPkQuery(metaData, object).toString();
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            int rows = statement.executeUpdate();
            if (rows == 1) {
                LOG.info("Entity {} has been updated successfully from the database.", entityType.getSimpleName());
            } else if (rows == 0) {
                throw new OrmException("There is no current entity in database. Nothing to update.");
            }
        } catch (Exception e) {
            LOG.error("There was an exception {}, during update query for {} ", e, entityType.getSimpleName());
            throw new OrmException(e);
        }
    }


    @Override
    public void deleteEntity(Object object) {
        Class<?> entityType = object.getClass();
        MetaData metaData = metaContext.getMetaDataMap().get(entityType);
        LOG.info("Try to delete entity {} from database.", entityType.getSimpleName());
        String deleteQuery = sqlManager.buildDeleteByPkQuery(metaData, object).toString();
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            int rows = statement.executeUpdate();
            if (rows == 1) {
                LOG.info("Entity {} has been delete successfully from the database.", entityType.getSimpleName());
            } else if (rows == 0) {
                throw new OrmException("There is no current entity in database. Nothing to delete.");
            }
        } catch (SQLException e) {
            LOG.error("There was an exception {}, during delete query for {} by {}", e, object.getClass().getSimpleName(), object.toString());
            throw new OrmException(e);
        }
    }

    @Override
    public Serializable insertEntity(Object object) {
        Class<?> entityType = object.getClass();
        MetaData entityMetaData = metaContext.getMetaDataMap().get(object.getClass());
        AbstractMetaPrimaryKey primaryKey = entityMetaData.getPrimaryKey();
        Serializable pkValue = null;
        try {
            pkValue = primaryKey.retrieveValue(object);
        } catch (IllegalAccessException e) {
            LOG.error("Couldn't get access to field: {}", e.getMessage());
        }

        LOG.info("Try to insert entity {} with primary key {} to database.", entityType.getSimpleName(), pkValue);

        InsertQuery insertQuery = null;

        if (Objects.isNull(pkValue)) {
            insertQuery = sqlManager.buildInsertQuery(entityMetaData, object);
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString(),
                    Statement.RETURN_GENERATED_KEYS)) {
                int affectedRow = preparedStatement.executeUpdate();
                if (affectedRow == 1) {
                    try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Serializable generatedId = primaryKey.parseIdValue(generatedKeys);
                            primaryKey.injectValue(generatedId, object);
                            LOG.info("Entity {} has been inserted successfully to the database with generated id {}.",
                                    entityType.getSimpleName(), generatedId);
                            return generatedId;
                        }
                    }
                } else {
                    LOG.error("Unexpected behaviour, entity {} wasn't inserted.", object.getClass().getSimpleName());
                    throw new IllegalStateException("Should insert only one row.");
                }
            } catch (SQLException | IllegalAccessException e) {
                LOG.error("There was an exception {}, during insert {}", e, object.getClass().getSimpleName());
                throw new OrmException(e);
            }
        }

        insertQuery = sqlManager.buildInsertQueryWithPk(entityMetaData, object, pkValue);
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString(),
                Statement.NO_GENERATED_KEYS)) {
            int rows = preparedStatement.executeUpdate();
            if (rows == 1) {
                LOG.info("Entity {} has been inserted successfully to the database with id {}.",
                        entityType.getSimpleName(), pkValue);
                return pkValue;
            } else {
                LOG.error("Unexpected behaviour, entity {} wasn't inserted.", object.getClass().getSimpleName());
                throw new IllegalStateException("Should insert only one row.");
            }
        } catch (SQLException e) {
            LOG.error("There was an exception {}, during insert {}", e, object.getClass().getSimpleName());
            throw new OrmException(e);
        }
    }

}
