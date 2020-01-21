package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface UserDao extends AbstractDao<User>{

    @Override
    Optional<User> get(Serializable id);

    @Override
    List<User> getAll();

    @Override
    void save(User user);

    @Override
    void update(User user);

    @Override
    void delete(User user);
}
