package com.softserve.easy;

import java.io.Serializable;

public interface Session {
    Serializable save(Object object);
    void update(Object object);
    void delete(Object object);
    <T> T get(Class<T> entityType, Serializable id);
    void close();
}
