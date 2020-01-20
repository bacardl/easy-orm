package com.softserve.easy.bind;

import com.softserve.easy.QueryConstant;
import com.softserve.easy.SimpleDbUnitTest;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import com.softserve.easy.jdbc.Persister;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import static com.softserve.easy.SimpleTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class EntityBinderSimpleTest extends SimpleDbUnitTest {
    @Mock
    private Persister persister;

    @InjectMocks
    private EntityBinder entityBinder;

    @Before
    public void setUp() {
        entityBinder = new EntityBinderImpl(getConfiguration().getMetaContext(), persister);
        MockitoAnnotations.initMocks(this);
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