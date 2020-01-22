package com.softserve.easy.client.dao;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.client.entity.Country;
import com.softserve.easy.core.SessionFactory;
import com.softserve.easy.core.SessionImpl;

import java.io.Serializable;
import java.util.Optional;

public class CountryDaoImpl implements CountryDao {
    private static SessionImpl session;

    public CountryDaoImpl() {
        Configuration configuration = new Configuration();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    @Override
    public Optional<Country> get(Serializable id) {
        return Optional.empty();
    }

    @Override
    public void save(Country country) {
        session.save(country);
    }

    @Override
    public void update(Country country) {
        session.update(country);
    }

    @Override
    public void delete(Country country) {
        session.delete(country);
    }
}
