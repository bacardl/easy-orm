package com.softserve.easy.core;

import com.softserve.easy.exception.OrmException;
import com.softserve.easy.meta.MetaContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionFactoryImpl implements SessionFactory {
    private final Map<Thread, Session> sessionsMap = new ConcurrentHashMap<>();
    private final MetaContext metaContext;
    private final DataSource dataSource;
    private boolean closed;

    public SessionFactoryImpl(DataSource dataSource, MetaContext metaContext) {
        this.dataSource = dataSource;
        this.metaContext = metaContext;
    }

    @Override
    public Session openSession() {
        try {
            Session session = new SessionImpl(dataSource.getConnection(), metaContext);
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
