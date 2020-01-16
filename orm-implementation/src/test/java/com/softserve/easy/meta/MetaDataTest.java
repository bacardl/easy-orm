package com.softserve.easy.meta;

import com.softserve.easy.entity.User;
import com.softserve.easy.helper.MetaDataParser;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MetaDataTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final MetaData META_DATA;

    static {
        META_DATA = MetaDataParser.analyzeClass(USER_CLASS);
        META_DATA.setMetaFields(MetaDataParser.createMetaFields(META_DATA));
    }

    @Test
    void getJoinedColumnNames() {
        assertThat(META_DATA.getJoinedInternalFieldsNames(), is("users.id,users.login,users.password,users.email"));
    }
}