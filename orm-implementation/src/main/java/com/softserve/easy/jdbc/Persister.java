package com.softserve.easy.jdbc;

import java.io.Serializable;

public interface Persister {

    <T> T getEntityById(Class<T> entityType, Serializable id);
    void updateEntity(Object object);

    void deleteEntity(Object object);
}
