package com.softserve.easy.core;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.Person;
import com.softserve.easy.simpleEntity.Country;
import com.softserve.easy.simpleEntity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.omg.PortableServer.IdAssignmentPolicyValue.USER_ID;

class SessionImplTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final String ID_FIELD_NAME = "id";
    private static SessionImpl session;

    @BeforeAll
    public static void init() {
        Configuration configuration = initTestConfiguration();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }



    @Test
    void deleteCascadeRecords() throws ParseException {
        Country country = new Country();
        country.setId(101);
        country.setName("Ukraine");

        Person person = new Person();
        person.setId(12L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setDateOfBirth(new SimpleDateFormat("yyyy-mm-dd").parse("1950-10-22"));

        User user = new User();
        user.setId(1L);
        user.setUsername("JohnDoe112");
        user.setPassword("password");
        user.setEmail("johnD@post.com");
        user.setCountry(country);
        session.delete(person);
        assertThat(session.get(USER_CLASS, USER_ID), Matchers.not(user));
    }


    private static Configuration initTestConfiguration() {
        return new Configuration();
    }


}