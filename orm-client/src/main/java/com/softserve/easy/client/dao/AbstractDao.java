package com.softserve.easy.client.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AbstractDao<T> {
    Optional<T> get(Serializable id);
    List<T> getAll();
    void save(T t);
    void update(T t);
    void delete(T t);
}
