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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"},
        executeScriptsAfter = {"dataset/simple/drop-db-schema.sql"})
public class SessionImplTestWithConnection {
    private static final Class<User> USER_CLASS = User.class;
    private static final Long USER_ID = 1L;
    private static SessionImpl session;

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
        Country country = new Country();
        country.setId(100);
        country.setName("United States");

        User user = new User();
        user.setId(1L);
        user.setUsername("Youghoss1978");
        user.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        user.setEmail("FredJPhillips@teleworm.us");
        user.setCountry(country);

        // user's equalsTo method has been override!
        assertThat(session.get(USER_CLASS, USER_ID), is(user));
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
    public void shouldReturnNullIfUserNotExist() {
        assertThat(session.get(USER_CLASS, 9999L), Matchers.nullValue());
    }


    @Test
    public void shouldBuildUserWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setLong(1, 1L);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = session.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getId(), equalTo(1L));
        assertThat(actualUser.getUsername(), equalTo("Youghoss1978"));
        assertThat(actualUser.getPassword(), equalTo("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG"));
        assertThat(actualUser.getEmail(), equalTo("FredJPhillips@teleworm.us"));
    }

    @Test
    public void shouldBuildUserWithExternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setLong(1, 1L);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = session.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getCountry().getId(), equalTo(100));
        assertThat(actualUser.getCountry().getName(), equalTo("United States"));
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