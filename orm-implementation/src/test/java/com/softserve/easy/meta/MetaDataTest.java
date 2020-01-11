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
        MetaDataBuilder metaDataBuilder = new MetaDataBuilder(USER_CLASS)
                .setMetaFields(MetaDataParser.createMetaFields(USER_CLASS))
                .setEntityDbName(MetaDataParser.getDbTableName(USER_CLASS).get())
                .setPrimaryKey(MetaDataParser.getPrimaryKeyField(USER_CLASS).get());
        META_DATA = metaDataBuilder.build();
    }

    @Test
    void getJoinedColumnNames() {
        assertThat(META_DATA.getJoinedColumnNames(), is("users.id,users.login,users.password,users.email"));
    }
}