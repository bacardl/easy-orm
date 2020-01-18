package com.softserve.easy.core;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.exception.OrmException;
import com.softserve.easy.simpleEntity.Country;
import com.softserve.easy.simpleEntity.User;
import org.hamcrest.Matchers;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"},
        executeScriptsAfter = {"dataset/simple/drop-db-schema.sql"})
public class SessionImplTestWithConnection {
    private static final Class<User> USER_CLASS = User.class;
    private static final Class<Country> COUNTRY_CLASS = Country.class;
    private static final Long USER_ID = 1L;
    private static final Integer COUNTRY_ID = 100;
    private static SessionImpl session;
    
    private static final User REFERENCE_USER;
    private static final Country REFERENCE_COUNTRY;

    static {
        REFERENCE_COUNTRY = new Country();
        REFERENCE_COUNTRY.setId(COUNTRY_ID);
        REFERENCE_COUNTRY.setName("United States");

        REFERENCE_USER = new User();
        REFERENCE_USER.setId(USER_ID);
        REFERENCE_USER.setUsername("Youghoss1978");
        REFERENCE_USER.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        REFERENCE_USER.setEmail("FredJPhillips@teleworm.us");
        REFERENCE_USER.setCountry(REFERENCE_COUNTRY);
    }

    @BeforeClass
    public static void init() {
        Configuration configuration = initTestConfiguration();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();

    private Connection getClientConnection() throws SQLException {
        return dbUnitRule.getDataSetExecutor().getRiderDataSource().getConnection();
    }

    @Test
    public void getUserByLongId() throws ParseException {
        assertThat(session.get(USER_CLASS, USER_ID), is(REFERENCE_USER));
    }


    @Test
    public void getCountryByLongId() throws ParseException {
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


    @Test
    public void shouldBuildUserWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setLong(1, USER_ID);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = session.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getId(), equalTo(REFERENCE_USER.getId()));
        assertThat(actualUser.getUsername(), equalTo(REFERENCE_USER.getUsername()));
        assertThat(actualUser.getPassword(), equalTo(REFERENCE_USER.getPassword()));
        assertThat(actualUser.getEmail(), equalTo(REFERENCE_USER.getEmail()));
    }

    @Test
    public void shouldBuildUserWithExternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setLong(1, USER_ID);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = session.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getCountry(), notNullValue());

        assertThat(actualUser.getCountry().getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualUser.getCountry().getName(), equalTo(REFERENCE_COUNTRY.getName()));
    }

    @Test
    public void shouldBuildCountryWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_COUNTRY_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setInt(1, COUNTRY_ID);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<Country> optionalCountry = session.buildEntity(COUNTRY_CLASS, resultSet);
        Country actualCountry = optionalCountry.orElseGet(Assertions::fail);

        assertThat(actualCountry.getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualCountry.getName(), equalTo(REFERENCE_COUNTRY.getName()));
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

        session.save(user);
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

    private static Configuration initTestConfiguration() {
        return new Configuration();
    }
}