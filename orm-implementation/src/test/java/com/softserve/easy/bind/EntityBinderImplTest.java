package com.softserve.easy.bind;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.softserve.easy.QueryConstant;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.simpleEntity.Country;
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
import static org.hamcrest.Matchers.notNullValue;


@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"},
        executeScriptsAfter = {"dataset/simple/drop-db-schema.sql"})
public class EntityBinderImplTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final Class<Country> COUNTRY_CLASS = Country.class;
    private static final Long USER_ID = 1L;
    private static final Integer COUNTRY_ID = 100;

    private static final User REFERENCE_USER;
    private static final Country REFERENCE_COUNTRY;

    private static EntityBinder entityBinder;

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

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance();

    @BeforeClass
    public static void initEntityBinder() {
        MetaContext metaContext = initTestConfiguration().getMetaContext();
        entityBinder = new EntityBinderImpl(metaContext);
    }

    private static Configuration initTestConfiguration() {
        return new Configuration();
    }

    private Connection getClientConnection() throws SQLException {
        return dbUnitRule.getDataSetExecutor().getRiderDataSource().getConnection();
    }


    @Test
    public void shouldBuildUserWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_1);
        ResultSet resultSet = preparedStatement.executeQuery();
        // important!
        resultSet.next();

        Optional<User> optionalUser = entityBinder.buildEntity(USER_CLASS, resultSet);
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
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_1);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = entityBinder.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getCountry(), notNullValue());

        assertThat(actualUser.getCountry().getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualUser.getCountry().getName(), equalTo(REFERENCE_COUNTRY.getName()));
    }

    @Test
    public void shouldBuildCountryWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_COUNTRY_BY_ID);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<Country> optionalCountry = entityBinder.buildEntity(COUNTRY_CLASS, resultSet);
        Country actualCountry = optionalCountry.orElseGet(Assertions::fail);

        assertThat(actualCountry.getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualCountry.getName(), equalTo(REFERENCE_COUNTRY.getName()));
    }
}