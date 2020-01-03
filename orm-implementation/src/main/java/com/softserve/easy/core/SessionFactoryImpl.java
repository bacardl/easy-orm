package com.softserve.easy.core;

import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionFactoryImpl implements SessionFactory{
    private final Map<Thread, Session> sessionsMap = new ConcurrentHashMap<>();
    private final DataSource dataSource;
    private final Map<Class<?>, MetaData> metaDataMap;
    private final DependencyGraph dependencyGraph;

    public SessionFactoryImpl(DataSource dataSource, Map<Class<?>, MetaData> metaDataMap, DependencyGraph dependencyGraph) {
        this.dataSource = dataSource;
        this.metaDataMap = metaDataMap;
        this.dependencyGraph = dependencyGraph;
    }

    @Override
    public Session openSession() {
        return null;
    }

    @Override
    public Session getCurrentSession() {
        return null;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void close() {

    }
}
