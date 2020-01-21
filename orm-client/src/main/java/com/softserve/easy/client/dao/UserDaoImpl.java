package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.User;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    @Override
    public Optional<User> get(Serializable id) {
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
