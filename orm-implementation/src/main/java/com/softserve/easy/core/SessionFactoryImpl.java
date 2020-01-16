package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.DependencyGraph;
import com.softserve.easy.meta.MetaData;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionFactoryImpl implements SessionFactory {
    private final Map<Thread, Session> sessionsMap = new ConcurrentHashMap<>();
    private final DataSource dataSource;
    private final Map<Class<?>, MetaData> metaDataMap;
    private final DependencyGraph dependencyGraph;

    private boolean closed;

    public SessionFactoryImpl(DataSource dataSource, Map<Class<?>, MetaData> metaDataMap, DependencyGraph dependencyGraph) {
        this.dataSource = dataSource;
        this.metaDataMap = metaDataMap;
        this.dependencyGraph = dependencyGraph;
    }

    @Override
    public Session openSession() {
        try {
            Session session = new SessionImpl(dataSource.getConnection(), metaDataMap, dependencyGraph);
            sessionsMap.put(Thread.currentThread(), session);
            return session;
        } catch (SQLException e) {
            throw new OrmException("Database connection failed!");
        }
    }

    @Override
    public Session getCurrentSession() {
        return sessionsMap.getOrDefault(Thread.currentThread(), this.openSession());
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void close() {
        sessionsMap.values().forEach(Session::close);
        this.closed = true;
    }
}
