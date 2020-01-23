package com.softserve.easy.core;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.ComplexDbUnitTest;
import com.softserve.easy.entity.complex.Country;
import com.softserve.easy.entity.complex.Order;
import com.softserve.easy.entity.complex.User;
import com.softserve.easy.exception.OrmException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static com.softserve.easy.ComplexTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class SessionComplexTestWithConnection extends ComplexDbUnitTest {
    private SessionImpl session;

    @Before
    public void init() {
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    // --------------------------------------read-----------------------------------------------------------------------
    @Test
    public void getUserByLongId() {
        assertThat(session.get(USER_CLASS, USER_ID), is(REFERENCE_USER));
    }

    @Test
    public void getPersonByLongId() {
        assertThat(session.get(PERSON_CLASS, PERSON_ID), is(REFERENCE_PERSON));
    }

    @Test
    public void getCountryByIntegerId() {
        assertThat(session.get(COUNTRY_CLASS, COUNTRY_ID), is(REFERENCE_COUNTRY));
    }

    @Test
    public void getOrderByLongId() {
        assertThat(session.get(ORDER_CLASS, ORDER_ID), is(REFERENCE_ORDER));
    }

    @Test
    public void assertThatOrderHasEagerLoadedUsersInstance() {
        Order order = session.get(ORDER_CLASS, ORDER_ID);
        User user = order.getUser();
        Country country = user.getCountry();
        System.out.println(country.getName());
    }

    @Test
    public void getProductByLongId() {
        assertThat(session.get(PRODUCT_CLASS, PRODUCT_ID), is(REFERENCE_PRODUCT));
    }

    @Test
    public void getOrderProductByEmbeddableId() {
        assertThat(session.get(ORDER_PRODUCT_CLASS, REFERENCE_ORDER_PRODUCT_ID), is(REFERENCE_ORDER_PRODUCT));
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

    // --------------------------------------read-----------------------------------------------------------------------


    // --------------------------------------create---------------------------------------------------------------------
    @ExpectedDataSet(value = "dataset/complex/yml/user/user-create.yml")
    @Test
    public void insertUserWithId() {
        User insertedUser = new User();
        insertedUser.setId(6L);
        insertedUser.setUsername("USERNAME_TEST");
        insertedUser.setPassword("PASSWORD_TEST");
        insertedUser.setCountry(REFERENCE_COUNTRY);
        Serializable returnedId = session.save(insertedUser);
        assertThat(returnedId, notNullValue());
        assertThat(returnedId, is(6L));
    }

    @ExpectedDataSet(value = "dataset/complex/yml/user/user-create.yml")
    @Test
    public void insertUserWithoutId() {
        User insertedUser = new User();
        insertedUser.setUsername("USERNAME_TEST");
        insertedUser.setPassword("PASSWORD_TEST");
        insertedUser.setCountry(REFERENCE_COUNTRY);
        Serializable returnedId = session.save(insertedUser);
        assertThat(returnedId, notNullValue());
        assertThat(returnedId, is(6L));
    }

    // --------------------------------------create---------------------------------------------------------------------

    // --------------------------------------update---------------------------------------------------------------------
    @ExpectedDataSet(value = "dataset/complex/yml/user/user-update.yml")
    @Test
    public void updateUserWithId() {
        User updatedUser = new User();
        updatedUser.setId(USER_ID);
        updatedUser.setUsername("UPDATED_VALUE");
        updatedUser.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        updatedUser.setEmail("FredJPhillips@teleworm.us");
        updatedUser.setPerson(REFERENCE_PERSON);
        updatedUser.setCountry(null);
        session.update(updatedUser);
    }

    @Test(expected = OrmException.class)
    public void updateUserWithoutId() {
        User updatedUser = new User();
        updatedUser.setId(null);
        updatedUser.setUsername("UPDATED_VALUE");
        updatedUser.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        updatedUser.setEmail("FredJPhillips@teleworm.us");
        updatedUser.setPerson(REFERENCE_PERSON);
        updatedUser.setCountry(null);
        session.update(updatedUser);
    }
    // --------------------------------------update---------------------------------------------------------------------

    // --------------------------------------delete---------------------------------------------------------------------
    @ExpectedDataSet(value = "dataset/complex/yml/user/user-delete.yml")
    @Test
    public void deleteUserWithId() {
        session.delete(REFERENCE_USER);
    }

    @Test(expected = OrmException.class)
    public void deleteUserWithoutId() {
        User deletedUser = new User();
        deletedUser.setId(null);
        deletedUser.setUsername("Youghoss1978");
        deletedUser.setPassword("$2y$10$RXyt4zu9H3PVKv5hE4Sln.FLsTgAakX5Ig7csH.0K58SwAwHVN8DG");
        deletedUser.setEmail("FredJPhillips@teleworm.us");
        deletedUser.setPerson(REFERENCE_PERSON);
        deletedUser.setCountry(REFERENCE_COUNTRY);
        session.delete(deletedUser);
    }
    // --------------------------------------delete---------------------------------------------------------------------



}
