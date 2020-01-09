package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SessionImpl implements Session {
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
        if (metaData.checkTypeCompatibility(entityType)) {
            throw new OrmException(String.format("The %s class isn't mapped by Orm", entityType.getSimpleName()));
        }
        if (metaData.checkIdCompatibility(id.getClass())) {
            throw new OrmException("Wrong type of ID object.");
        }
        String sqlQuery = buildSelectSqlQuery(entityType);
        return null;
    }

    private String buildSelectSqlQuery(Class<?> entityType) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Set<Class<?>>> dependencies = dependencyGraph.getLayeredDependencies(entityType);
        for (Set<Class<?>> dependency : dependencies) {

        }
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
