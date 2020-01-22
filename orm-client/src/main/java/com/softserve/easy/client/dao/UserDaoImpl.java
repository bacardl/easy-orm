package com.softserve.easy.client.dao;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.client.entity.User;
import com.softserve.easy.core.Session;
import com.softserve.easy.core.SessionFactory;
import com.softserve.easy.core.SessionImpl;

import java.io.Serializable;
import java.util.Optional;

public class UserDaoImpl implements UserDao{
    private Session session;

    public UserDaoImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<User> get(Serializable id) {
        //return session.get(User.class, id);
        return null;
    }

    @Override
    public void save(User user) {
        session.save(user);
    }

    @Override
    public void update(User user) {
        session.update(user);
    }

    @Override
    public void delete(User user) {
        session.delete(user);
    }
}
