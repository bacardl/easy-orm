package com.softserve.easy.core;

import com.softserve.easy.ComplexDbUnitTest;
import com.softserve.easy.exception.OrmException;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static com.softserve.easy.ComplexTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SessionComplexTestWithConnection extends ComplexDbUnitTest {
    private SessionImpl session;

    @Before
    public void init() {
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        session = (SessionImpl) sessionFactory.openSession();
    }

    @Test
    public void getUserByLongId() {
        assertThat(session.get(USER_CLASS, USER_ID), is(REFERENCE_USER));
    }


    @Test
    public void getCountryByLongId() {
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

}
