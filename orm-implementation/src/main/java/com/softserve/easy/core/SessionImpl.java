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
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException("The arguments cannot be null.");
        }
        MetaData metaData = metaContext.getMetaDataMap().get(object.getClass());
        if (Objects.isNull(metaData)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", object.getClass().getSimpleName()));
        }

        return persister.insertEntity(object);
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
