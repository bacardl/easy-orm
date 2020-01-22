package com.softserve.easy.core;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.SimpleDbUnitTest;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import com.softserve.easy.exception.OrmException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.text.ParseException;

import static com.softserve.easy.SimpleTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


public class SessionSimpleTestWithConnection extends SimpleDbUnitTest {
    private SessionImpl session;

    @Before
    public void init() {
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    @Test
    public void getUserByLongId() {
        assertThat(session.get(USER_CLASS, USER_ID), is(REFERENCE_USER));
    }


    @Test
    public void getCountryByLongId() {
        assertThat(session.get(COUNTRY_CLASS, COUNTRY_ID), is(REFERENCE_COUNTRY));
    }


    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenIdIsNull() {
        session.get(USER_CLASS, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionWhenClassIsNull() {
        session.get(null, USER_ID);
    }

    @Test(expected = OrmException.class)
    public void throwExceptionWhenObjectsTypeIsNotCompatibilityWithOrm() {
        session.get(Object.class, USER_ID);
    }

    @Test(expected = OrmException.class)
    public void throwExceptionWhenObjectsTypeIsNotCompatibilityWithPk() {
        session.get(USER_CLASS, "fail");
    }

    @Test
    public void shouldReturnNullIfUserNotExistInDatabase() {
        assertThat(session.get(USER_CLASS, 9999L), Matchers.nullValue());
    }

    @ExpectedDataSet(value = "dataset/simple/yml/data-delete.yml")
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
        session.delete(user);
    }

    @ExpectedDataSet(value = "dataset/simple/yml/data-update.yml")
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
        session.update(user);
    }
    @ExpectedDataSet(value = "dataset/simple/yml/data.yml")
    @Test(expected = OrmException.class)
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
        session.update(user);
    }

    //INSERT
    @ExpectedDataSet(value = "dataset/simple/yml/data-insert.yml")
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

        Serializable id = session.save(user);
        assertThat(id, notNullValue());
        assertThat(id, is(6L));
    }

    @ExpectedDataSet(value = "dataset/simple/yml/data-insert.yml")
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

        session.save(user);
    }

}