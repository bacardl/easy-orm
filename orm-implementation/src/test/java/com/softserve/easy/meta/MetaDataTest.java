package com.softserve.easy.meta;

import com.softserve.easy.entity.User;
import com.softserve.easy.helper.MetaDataParser;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MetaDataTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final MetaData META_DATA;
    public static final long NUMBER_OF_INTERNAL_FIELDS_FOR_CLASS_USER = 4L;
    public static final long NUMBER_OF_EXTERNAL_FIELDS_FOR_CLASS_USER = 2L;

    static {
        META_DATA = MetaDataParser.analyzeClass(USER_CLASS);
        META_DATA.setMetaFields(MetaDataParser.createMetaFields(META_DATA));
    }

    @Test
    void getJoinedColumnNames() {
        assertThat(META_DATA.getJoinedInternalFieldsNames(), is("users.id,users.login,users.password,users.email"));
    }

    @Test
    void shouldReturnNumberOfInternalFieldsForUser() {
        assertThat(META_DATA.getCountInternalFields(), is(NUMBER_OF_INTERNAL_FIELDS_FOR_CLASS_USER));
    }

    @Test
    void shouldReturnNumberOfIExternalFieldsForUser() {
        assertThat(META_DATA.getCountExternalFields(), is(NUMBER_OF_EXTERNAL_FIELDS_FOR_CLASS_USER));
    }
}