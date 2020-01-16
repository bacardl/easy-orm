package com.softserve.easy.core;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.simpleEntity.Country;
import com.softserve.easy.simpleEntity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

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
    void getSelectSqlStringForUserClass() {
        assertThat(cleanUpString(session.buildSelectSqlQuery(USER_CLASS)),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USERS_QUERY_WITHOUT_SCHEMA_NAME)));
    }

    @Test
    void getSelectSqlStringForUserClassById() {
        assertThat(cleanUpString(session.buildSelectSqlQueryWithWhereClause(USER_CLASS, ID_FIELD_NAME)),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME)));
    }

    @Test
    void getSelectSqlStringForCountryClass() {
        assertThat(cleanUpString(session.buildSelectSqlQuery(Country.class)),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_COUNTRY_QUERY_WITHOUT_SCHEMA_NAME)));
    }


    private static Configuration initTestConfiguration() {
        return new Configuration();
    }

    private static String cleanUpString(String input) {
        return input.replaceAll("[\\s\\n\\t\\r\\f\\v]+", "").trim();
    }

    @Test
    void shouldReturnInsertQueryForUserWithId() {
        Country ukr = new Country();
        ukr.setId(13);
        ukr.setName("Ukraine");

        User jon = new User();
        jon.setId((long)404);
        jon.setUsername("jon");
        jon.setPassword("jon111");
        jon.setEmail("jon@gmail.com");
        jon.setCountry(ukr);

        assertThat(cleanUpString(session.buildInsertSqlQuery(jon)),
                equalToIgnoringCase(cleanUpString(QueryConstant.INSERT_USER_QUERY_WITH_ID)));
    }

    @Test
    void shouldReturnInsertQueryForCountryWithId() {
        Country ukr = new Country();
        ukr.setId(13);
        ukr.setName("Ukraine");

        assertThat(cleanUpString(session.buildInsertSqlQuery(ukr)),
                equalToIgnoringCase(cleanUpString(QueryConstant.INSERT_COUNTRY_QUERY_WITH_CODE)));
    }

    @Test
    void shouldReturnInsertQueryForUserWithoutId() {
        Country ukr = new Country();
        ukr.setId(13);
        ukr.setName("Ukraine");

        User jon = new User();
        jon.setUsername("jon");
        jon.setPassword("jon111");
        jon.setEmail("jon@gmail.com");
        jon.setCountry(ukr);

        assertThat(cleanUpString(session.buildInsertSqlQuery(jon)),
                equalToIgnoringCase(cleanUpString(QueryConstant.INSERT_USER_QUERY_WITHOUT_ID)));
    }

    @Test
    void shouldReturnInsertQueryForCountryWithoutId() {
        Country ukr = new Country();
        ukr.setName("Ukraine");

        assertThat(cleanUpString(session.buildInsertSqlQuery(ukr)),
                equalToIgnoringCase(cleanUpString(QueryConstant.INSERT_COUNTRY_QUERY_WITHOUT_CODE)));
    }
}