package com.softserve.easy.jdbc;

import java.io.Serializable;

public interface Persister {

    <T> T getEntityById(Class<T> entityType, Serializable id);
    <T> T getLazyEntityById(Class<T> entityType, Serializable id);
    Serializable insertEntity(Object object);
    void updateEntity(Object object);
    void deleteEntity(Object object);
}
