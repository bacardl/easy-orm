package com.softserve.easy.jdbc;

import java.io.Serializable;

public interface Persister {

    <T> T getEntityById(Class<T> entityType, Serializable id);
    void updateEntity(Object object);
    void insertEntity(Object object);
    void insertEntityWithId(Object object, Serializable id);
    void deleteEntity(Object object);
}
