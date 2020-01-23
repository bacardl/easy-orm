package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.Country;
import com.softserve.easy.core.Session;

import java.io.Serializable;
import java.util.Optional;

public class CountryDaoImpl implements CountryDao {
    private Session session;

    public CountryDaoImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Country> get(Serializable id) {
        return Optional.of(session.get(Country.class, id));
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
