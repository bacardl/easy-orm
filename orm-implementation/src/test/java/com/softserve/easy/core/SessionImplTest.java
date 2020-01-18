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
import static org.hamcrest.Matchers.equalToIgnoringCase;
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
    public void generateDeleteQuery(){
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(3L);
        user.setUsername("Wittiould1980");
        user.setPassword("$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2");
        user.setEmail("VirginiaDCook@armyspy.com");
        user.setCountry(country);
        String actual = session.buildDeleteSqlQuery(user);
        assertThat(cleanUpString(session.buildDeleteSqlQuery(user)),
                equalToIgnoringCase(cleanUpString(QueryConstant.DELETE_USER_QUERY_WHERE_PRIMARYKEY)));
    }
    @Test
    void generateUpdateQuery(){
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(3L);
        user.setUsername("Wittiould1980");
        user.setPassword("$2y$10$pRmUEPJMB0/CR5uCuqCA2ODdE0iOJswpXFdIIhZmuZyiZIMe.OCl2");
        user.setEmail("VOT_ETO_DA@armyspy.com");
        user.setCountry(country);
        String actual = session.buildUpdateQuery(user.getClass());
        System.out.println(actual);
        assertThat(actual, Matchers.notNullValue());
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