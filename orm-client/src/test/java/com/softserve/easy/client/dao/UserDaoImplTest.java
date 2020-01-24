package com.softserve.easy.client.dao;

import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.softserve.easy.client.entity.Country;
import com.softserve.easy.client.entity.User;
import com.softserve.easy.core.Session;
import com.softserve.easy.core.SessionFactory;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

//@RunWith(JUnit4.class)
//@DataSet(
//        value = "dataset/complex/yml/user/user.yml",
//        strategy = SeedStrategy.INSERT, cleanAfter = true,
//        executeScriptsBefore = {"dataset/complex/db-schema.sql"},
//        executeScriptsAfter = {"dataset/complex/drop-db-schema.sql"})

public class UserDaoImplTest extends DbUnitTest {
    UserDaoImpl dao;

    @Before
    public void init() {
        SessionFactory sessionFactory = getConfiguration().buildSessionFactory();
        Session session = sessionFactory.openSession();
//        Configuration configuration = new Configuration();
//        SessionFactory sessionFactory = configuration.buildSessionFactory();
//        Session session = sessionFactory.openSession();
        dao = new UserDaoImpl(session);
    }

//    @Test
//    void getUserByLongId() {
//       assertThat(dao.get(USER_ID), is(REFERENCE_USER));
//    }

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
        dao.delete(user);
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
        dao.update(user);
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
        dao.update(user);
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

        dao.save(user);
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

        dao.save(user);
    }
}