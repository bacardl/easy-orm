package com.softserve.easy.core;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.simpleEntity.User;
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
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.CLEAN_INSERT,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"})
public class SessionImplTestWithConnection {
    private static final Class<User> USER_CLASS = User.class;
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
    public void myTest() throws SQLException {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setObject(1, new Long(1L));

        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        System.out.println(resultSet.isLast());
        System.out.println("ID: " + resultSet.getLong("id"));
        System.out.println("ID like Long obj: " + resultSet.getObject("id", Long.class));
    }

    @Test
    public void shouldCreateUserWithInternalFields() throws SQLException {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_WITHOUT_SCHEMA_NAME);
        preparedStatement.setLong(1, 1L);

        ResultSet resultSet = preparedStatement.executeQuery();

        Optional<User> optionalUser = session.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getId(), equalTo(1L));
        assertThat(actualUser.getUsername(), equalTo("Youghoss1978"));
        assertThat(actualUser.getPassword(), equalTo("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG"));
        assertThat(actualUser.getEmail(), equalTo("FredJPhillips@teleworm.us"));
    }

    private static Configuration initTestConfiguration() {
        return new Configuration();
    }
}