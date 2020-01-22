package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.User;

import java.io.Serializable;
import java.util.Optional;

public interface UserDao extends Dao<User> {

    @Override
    Optional<User> get(Serializable id);

    @Override
    void save(User user);

    @Override
    void update(User user);

    @Override
    void delete(User user);
}
