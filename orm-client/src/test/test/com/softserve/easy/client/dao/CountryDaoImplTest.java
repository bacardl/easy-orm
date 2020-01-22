package com.softserve.easy.client.dao;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.client.entity.Country;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

class CountryDaoImplTest {
    CountryDaoImpl dao;

    @Before
    public void init() {
        //dao = new CountryDaoImpl();
    }

    @Test
    void get() {
    }

    @ExpectedDataSet(value = "ymlCountry/data-delete.yml")
    @Test
    public void shouldDeleteCountryWithId(){
        Country country = new Country();
        country.setId(400);
        country.setName("Zambia");

        dao.delete(country);
    }

    @ExpectedDataSet(value = "ymlCountry/data-update.yml")
    @Test
    public void shouldUpdateCountry(){
        Country country = new Country();
        country.setId(400);
        country.setName("Colombia");

        dao.update(country);
    }

    //INSERT
    @ExpectedDataSet(value = "ymlCountry/data-insert.yml")
    @Test
    public void saveCountryWithoutId() throws ParseException {
        Country country = new Country();
        country.setName("Colombia");

        dao.save(country);
    }

    @ExpectedDataSet(value = "ymlCountry/data-insert.yml")
    @Test
    public void saveCountryWithId() throws ParseException {
        Country country = new Country();
        country.setId(600);
        country.setName("Colombia");

        dao.save(country);
    }
}