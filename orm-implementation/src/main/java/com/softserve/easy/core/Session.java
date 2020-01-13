package com.softserve.easy.core;

import java.io.Serializable;

public interface Session extends Transactional {
    Serializable save(Object object);
    <T> T get(Class<T> entityType, Serializable id);
    void update(Object object);
    void delete(Object object);
    void clear();
    void flush();
    void close();
    String insert(Object object) throws IllegalAccessException;
}
