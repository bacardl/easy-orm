package com.softserve.easy.core;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.simpleEntity.Country;
import com.softserve.easy.simpleEntity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;

class SessionImplTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final long USER_ID = 1L;
    private static SessionImpl session;

    @BeforeAll
    public static void init() {
        Configuration configuration = initTestConfiguration();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    @Test
    void getSelectSqlStringForUserClass() {
        assertThat(cleanUpString(session.buildSelectSqlQuery(User.class)),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USERS_QUERY_WITHOUT_SCHEMA_NAME)));
    }

    @Test
    void getSelectSqlStringForCountryClass() {
        assertThat(cleanUpString(session.buildSelectSqlQuery(Country.class)),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_COUNTRY_QUERY_WITHOUT_SCHEMA_NAME)));
    }

    @Test
    void getUserByLongId() throws ParseException {
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(1L);
        user.setUsername("Youghoss1978");
        user.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        user.setEmail("FredJPhillips@teleworm.us");
        user.setCountry(country);

        assertThat(session.get(USER_CLASS, USER_ID), is(user));
    }

    private static Configuration initTestConfiguration() {
        return new Configuration();
    }

    private static String cleanUpString(String input) {
        return input.replaceAll("[\\s\\n\\t\\r\\f\\v]+", "");
    }

}