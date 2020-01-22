package com.softserve.easy.client.dao;

import com.softserve.easy.client.entity.Country;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CountryDao extends AbstractDao<Country> {

    @Override
    Optional<Country> get(Serializable id);

    @Override
    void save(Country country);

    @Override
    void update(Country country);

    @Override
    void delete(Country country);
}
