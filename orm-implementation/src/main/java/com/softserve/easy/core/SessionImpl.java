package com.softserve.easy.core;

import java.io.Serializable;
import java.sql.Connection;

public class SessionImpl implements Session {
    private Connection connection;
    private Transaction transaction;

    public SessionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Serializable save(Object object) {
        return null;
    }

    @Override
    public <T> T get(Class<T> entityType, Serializable id) {
        return null;
    }

    @Override
    public void update(Object object) {

    }

    @Override
    public void delete(Object object) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void flush() {

    }

    @Override
    public void close() {

    }

    @Override
    public Transaction beginTransaction() {
        return null;
    }
}
