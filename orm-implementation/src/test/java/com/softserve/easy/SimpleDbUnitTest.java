package com.softserve.easy;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.SeedStrategy;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.softserve.easy.constant.ConfigPropertyConstant.ENTITY_PACKAGE_PROPERTY;

@RunWith(JUnit4.class)
@DataSet(
        value = "dataset/simple/yml/data.yml",
        strategy = SeedStrategy.INSERT, cleanAfter = true,
        executeScriptsBefore = {"dataset/simple/db-schema.sql"},
        executeScriptsAfter = {"dataset/simple/drop-db-schema.sql"})
public abstract class SimpleDbUnitTest extends DbUnitTest {
    protected static final Class<User> USER_CLASS = User.class;
    protected static final Class<Country> COUNTRY_CLASS = Country.class;
    protected static final Long USER_ID = 1L;
    protected static final Integer COUNTRY_ID = 100;

    protected static final User REFERENCE_USER;
    protected static final Country REFERENCE_COUNTRY;

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

    private static final String SIMPLE_ENTITY_PACKAGE = "com.softserve.easy.entity.simple";

    public SimpleDbUnitTest() {
        getConfiguration().setProperty(ENTITY_PACKAGE_PROPERTY, SIMPLE_ENTITY_PACKAGE);
    }
}
