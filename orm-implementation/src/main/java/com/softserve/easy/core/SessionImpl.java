package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.jdbc.JDBCPersister;
import com.softserve.easy.jdbc.Persister;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Objects;


public class SessionImpl implements Session {
    private static final Logger LOG = LoggerFactory.getLogger(SessionImpl.class);

    private final Connection connection;
    private final MetaContext metaContext;
    private final Persister persister;

    private Transaction transaction;

    public SessionImpl(Connection connection, MetaContext metaContext) {
        this.connection = connection;
        this.metaContext = metaContext;
        this.persister = new JDBCPersister(connection, metaContext);
    }

    @Override
    public Serializable save(Object object) {
        // checks Object
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
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
        MetaData currentMetaData = metaContext.getMetaDataMap().get(object.getClass());

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
        MetaData currentMetaData = metaContext.getMetaDataMap().get(object.getClass());
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

        return persister.getEntityById(entityType, id);
    }

    @Override
    public void update(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }

        persister.updateEntity(object);
    }

    @Override
    public void delete(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The argument cannot be null.");
        }
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }

        persister.deleteEntity(object);
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
