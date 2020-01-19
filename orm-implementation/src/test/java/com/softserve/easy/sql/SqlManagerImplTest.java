package com.softserve.easy.sql;

import com.softserve.easy.QueryConstant;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import com.softserve.easy.meta.MetaContext;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

class SqlManagerImplTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final Class<Country> COUNTRY_CLASS = Country.class;

    private static SqlManagerImpl sqlManagerImpl;

    @BeforeAll
    static void initSqlManager(){
        MetaContext metaContext = new Configuration().getMetaContext();
        sqlManagerImpl = new SqlManagerImpl(metaContext);
    }

    @Test
    void getSelectSqlStringForUserClass() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectAllSqlQuery(USER_CLASS).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USERS_QUERY)));
    }

    @Test
    void getSelectSqlStringForUserClassById() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectByIdSqlQuery(USER_CLASS, 1L).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USER_BY_ID_1)));
    }

    @Test
    void getSelectSqlStringForCountryClass() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectAllSqlQuery(COUNTRY_CLASS).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_COUNTRY_QUERY)));
    }

    @Test
    void getSelectSqlStringForCountryClassById() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectByIdSqlQuery(COUNTRY_CLASS, 100).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_COUNTRY_BY_ID)));
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
        assertThat(cleanUpString(sqlManagerImpl.buildDeleteSqlQuery(user)),
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
        assertThat(sqlManagerImpl.buildUpdateQuery(user.getClass()), Matchers.notNullValue());
    }

    private static String cleanUpString(String input) {
        return input.replaceAll("[\\s\\n\\t\\r\\f\\v]+", "").trim();
    }
}