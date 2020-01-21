package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.Country;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class CountryDaoImpl implements CountryDao {
    @Override
    public Optional<Country> get(Serializable id) {
        return Optional.empty();
    }

    @Override
    public List<Country> getAll() {
        return null;
    }

    @Override
    public void save(Country country) {

    }

    @Override
    public void update(Country country) {

    }

    @Override
    public void delete(Country country) {

    }
}
