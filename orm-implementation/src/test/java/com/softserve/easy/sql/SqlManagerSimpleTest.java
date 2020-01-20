package com.softserve.easy.sql;

import com.softserve.easy.QueryConstant;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.softserve.easy.SimpleTestEnvironment.*;
import static com.softserve.easy.constant.ConfigPropertyConstant.ENTITY_PACKAGE_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

class SqlManagerSimpleTest {
    private static SqlManagerImpl sqlManagerImpl;
    private static MetaContext metaContext;
    private static MetaData userMeta;
    private static MetaData countryMeta;


    @BeforeAll
    static void initSqlManager()
    {   Configuration configuration = new Configuration();
        configuration.setProperty(ENTITY_PACKAGE_PROPERTY, "com.softserve.easy.entity.simple");
        metaContext = configuration.getMetaContext();
        sqlManagerImpl = new SqlManagerImpl(metaContext);
        userMeta = metaContext.getMetaDataMap().get(USER_CLASS);
        countryMeta = metaContext.getMetaDataMap().get(COUNTRY_CLASS);
    }


    @Test
    void getSelectSqlStringForUserClass() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectAllQuery(userMeta).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USERS_QUERY)));
    }

    @Test
    void getSelectSqlStringForUserClassById() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectByPkQuery(userMeta, 1L).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_USER_BY_ID_1)));
    }

    @Test
    void getSelectSqlStringForCountryClass() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectAllQuery(countryMeta).toString()),
                equalToIgnoringCase(cleanUpString(QueryConstant.SELECT_COUNTRY_QUERY)));
    }

    @Test
    void getSelectSqlStringForCountryClassById() {
        assertThat(cleanUpString(sqlManagerImpl.buildSelectByPkQuery(countryMeta, 100).toString()),
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
        String deleteQuery = sqlManagerImpl.buildDeleteByPkQuery(userMeta, user).toString();
        assertThat(cleanUpString(deleteQuery),
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
        String updateQuery = sqlManagerImpl.buildUpdateByPkQuery(userMeta, user).toString();
        System.out.println(updateQuery);
        assertThat(cleanUpString(updateQuery),
                equalToIgnoringCase(cleanUpString(QueryConstant.UPDATE_USER_QUERY_WHERE_PRIMARYKEY)));
    }

    @Test
    void shouldReturnInsertQueryForUserWithId() {
        User user = new User();
        user.setId(404L);
        user.setUsername("jon");
        user.setPassword("jon111");
        user.setEmail("jon@gmail.com");
        user.setCountry(REFERENCE_COUNTRY);

        assertThat(cleanUpString(sqlManagerImpl.buildInsertQueryWithPk(userMeta, user, 404L).toString()),
                equalToIgnoringCase(cleanUpString(com.softserve.easy.core.QueryConstant.INSERT_USER_QUERY_WITH_ID)));
    }

    @Test
    void shouldReturnInsertQueryForCountryWithId() {
        Country country = new Country();
        country.setId(900);
        country.setName("Japan");
        assertThat(cleanUpString(sqlManagerImpl.buildInsertQueryWithPk(countryMeta, country, 900).toString()),
                equalToIgnoringCase(cleanUpString(com.softserve.easy.core.QueryConstant.INSERT_COUNTRY_QUERY_WITH_CODE)));
    }

    @Test
    void shouldReturnInsertQueryForUserWithoutId() {
        User user = new User();
        user.setUsername("jon");
        user.setPassword("jon111");
        user.setEmail("jon@gmail.com");
        user.setCountry(REFERENCE_COUNTRY);

        assertThat(cleanUpString(sqlManagerImpl.buildInsertQuery(userMeta, user).toString()),
                equalToIgnoringCase(cleanUpString(com.softserve.easy.core.QueryConstant.INSERT_USER_QUERY_WITHOUT_ID)));
    }

    @Test
    void shouldReturnInsertQueryForCountryWithoutId() {
        Country country = new Country();
        country.setName("Japan");

        assertThat(cleanUpString(sqlManagerImpl.buildInsertQuery(countryMeta, country).toString()),
                equalToIgnoringCase(cleanUpString(com.softserve.easy.core.QueryConstant.INSERT_COUNTRY_QUERY_WITHOUT_CODE)));
    }

    private static String cleanUpString(String input) {
        return input.replaceAll("[\\s\\n\\t\\r\\f\\v]+", "").trim();
    }
}