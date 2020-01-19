package com.softserve.easy.meta;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.simple.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.softserve.easy.constant.ConfigPropertyConstant.ENTITY_PACKAGE_PROPERTY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MetaDataTest {
    private static final Class<User> USER_CLASS = User.class;
    public static final long NUMBER_OF_INTERNAL_FIELDS_FOR_CLASS_USER = 4L;
    public static final long NUMBER_OF_EXTERNAL_FIELDS_FOR_CLASS_USER = 2L;
    public static final String STRING_OF_INTERNAL_FIELDS_WITHOUT_PK_CLASS_USER = "users.login,users.password,users.email";

    private static MetaData META_USER_DATA;

    @BeforeAll
    static void initMeta()
    {   Configuration configuration = new Configuration();
        configuration.setProperty(ENTITY_PACKAGE_PROPERTY, "com.softserve.easy.entity.simple");
        MetaContext metaContext = configuration.getMetaContext();
        META_USER_DATA = metaContext.getMetaDataMap().get(USER_CLASS);
//        countryMeta = metaContext.getMetaDataMap().get(COUNTRY_CLASS);
    }

    @Test
    void getJoinedColumnNames() {
        assertThat(META_USER_DATA.getJoinedInternalFieldsNames(), is("users.id,users.login,users.password,users.email"));
    }

    @Test
    void shouldReturnNumberOfInternalFieldsForUser() {
        assertThat(META_USER_DATA.getCountInternalFields(), is(NUMBER_OF_INTERNAL_FIELDS_FOR_CLASS_USER));
    }

    @Test
    void shouldReturnNumberOfExternalFieldsForUser() {
        assertThat(META_USER_DATA.getCountExternalFields(), is(NUMBER_OF_EXTERNAL_FIELDS_FOR_CLASS_USER));
    }

    @Test
    void shouldReturnListOfInternalFieldsWithoutPrimaryKeyForUser() {
        assertThat(META_USER_DATA.getJoinedInternalFieldsNamesWithoutPrimaryKey(), is(STRING_OF_INTERNAL_FIELDS_WITHOUT_PK_CLASS_USER));
    }
}