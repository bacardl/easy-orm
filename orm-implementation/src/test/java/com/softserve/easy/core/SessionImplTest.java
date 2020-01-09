package com.softserve.easy.core;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.Country;
import com.softserve.easy.entity.Person;
import com.softserve.easy.entity.User;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;

class SessionImplTest {

    private static final Class<User> USER_CLASS = User.class;
    private static final long USER_ID = 1L;
    private static Session session;

    @BeforeClass
    public static void init() {
        Configuration configuration = initTestConfiguration();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = sessionFactory.openSession();
    }

    @Test
    void getUserByLongId() throws ParseException {
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        Person person = new Person();
        person.setId(1L);
        person.setFirstName("Fred");
        person.setLastName("Phillips");
        person.setDateOfBirth(new SimpleDateFormat("yyyy-mm-dd").parse("1990-01-20"));

        User user = new User();
        user.setId(1L);
        user.setUsername("Youghoss1978");
        user.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        user.setEmail("FredJPhillips@teleworm.us");
        user.setCountry(country);
        user.setPerson(person);

        assertThat(session.get(USER_CLASS, USER_ID), Matchers.is(user));
    }

    private static Configuration initTestConfiguration() {
        return new Configuration();
    }

}