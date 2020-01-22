package com.softserve.easy.client.dao;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.client.entity.Country;
import com.softserve.easy.client.entity.User;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

class UserDaoImplTest {
    UserDaoImpl dao;

    @Before
    public void init() {
        //dao = new UserDaoImpl();
    }

    @Test
    void get() {
    }

    @ExpectedDataSet(value = "ymlUser/data-delete.yml")
    @Test
    public void shouldDeleteUserWithId(){
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(3L);
        user.setUsername("Wittiould1980");
        user.setPassword("$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2");
        user.setEmail("VirginiaDCook@armyspy.com");
        user.setCountry(country);
        dao.delete(user);
    }

    @ExpectedDataSet(value = "ymlUser/data-update.yml")
    @Test
    public void shouldUpdateUser(){
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(3L);
        user.setUsername("Wittiould1980");
        user.setPassword("$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2");
        user.setEmail("VOT_ETO_DA@armyspy.com");
        user.setCountry(country);
        dao.update(user);
    }

    @ExpectedDataSet(value = "ymlUser/data.yml")
    @Test
    public void shouldNotUpdateUser(){
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(28L);
        user.setUsername("Wittiould1980");
        user.setPassword("$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2");
        user.setEmail("VOT_ETO_DA@armyspy.com");
        user.setCountry(country);
        dao.update(user);
    }

    //INSERT
    @ExpectedDataSet(value = "ymlUser/data-insert.yml")
    @Test
    public void saveUserWithoutId() throws ParseException {
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setUsername("Jon");
        user.setPassword("Jon123123");
        user.setEmail("Jon@gmail.com");
        user.setCountry(country);

        dao.save(user);
    }

    @ExpectedDataSet(value = "ymlUser/data-insert.yml")
    @Test
    public void saveUserWithId() throws ParseException {
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(6L);
        user.setUsername("Jon");
        user.setPassword("Jon123123");
        user.setEmail("Jon@gmail.com");
        user.setCountry(country);

        dao.save(user);
    }
}