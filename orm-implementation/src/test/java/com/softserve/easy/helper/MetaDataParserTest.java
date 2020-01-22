package com.softserve.easy.helper;

import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.constant.PrimaryKeyType;
import com.softserve.easy.entity.complex.Country;
import com.softserve.easy.entity.complex.Order;
import com.softserve.easy.entity.complex.User;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.AbstractMetaField;
import com.softserve.easy.meta.field.CollectionMetaField;
import com.softserve.easy.meta.field.ExternalMetaField;
import com.softserve.easy.meta.field.InternalMetaField;
import com.softserve.easy.meta.primarykey.AbstractMetaPrimaryKey;
import com.softserve.easy.meta.primarykey.SinglePrimaryKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MetaDataParserTest {

    private static final Class<User> USER_CLASS = User.class;
    private static final int NUMBER_OF_FIELDS_OF_USER_CLASS = 6;
    private static final String USERNAME_FIELD_DB_NAME = "login";
    private static final Class<Country> TYPE_OF_COUNTRY_FIELD = Country.class;
    private static final Class<Set> TYPE_OF_ORDERS_FIELD = Set.class;
    private static final Class<Order> GENERIC_TYPE_OF_ORDERS_FIELD = Order.class;
    private static Field ID_FIELD;
    private static Field USERNAME_FIELD;
    private static Field PASSWORD_FIELD;
    private static Field EMAIL_FIELD;
    private static Field PERSON_FIELD;
    private static Field COUNTRY_FIELD;
    private static Field ORDERS_FIELD;

    private static MetaData userMetaData;

    @BeforeAll
    static void init() {
        try {
            // primary key
            ID_FIELD = User.class.getDeclaredField("id");

            USERNAME_FIELD = User.class.getDeclaredField("username");
            PASSWORD_FIELD = User.class.getDeclaredField("password");
            EMAIL_FIELD = User.class.getDeclaredField("email");
            PERSON_FIELD = User.class.getDeclaredField("person");
            COUNTRY_FIELD = User.class.getDeclaredField("country");
            ORDERS_FIELD = User.class.getDeclaredField("orders");
        } catch (NoSuchFieldException e) {
            Assertions.fail("The fields should follow the structure User.class");
        }
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(USER_CLASS);
        MetaContext metaContext = configuration.getMetaContext();
        userMetaData = metaContext.getMetaDataMap().get(USER_CLASS);
    }

    @Test
    void shouldInitializeSinglePrimaryKey() {
        AbstractMetaPrimaryKey primaryKey = userMetaData.getPrimaryKey();
        assertThat(primaryKey, notNullValue());
        assertThat(primaryKey, instanceOf(SinglePrimaryKey.class));
        assertThat(primaryKey.getField(), is(ID_FIELD));
        assertThat(primaryKey.getPrimaryKeyType(), is(PrimaryKeyType.SINGLE));
    }

    @Test
    void shouldContainMetaFields() {
        Map<Field, AbstractMetaField> metaFields = userMetaData.getMetaFields();
        assertThat(metaFields, aMapWithSize(NUMBER_OF_FIELDS_OF_USER_CLASS));

        assertThat(metaFields, hasKey(USERNAME_FIELD));
        assertThat(metaFields, hasKey(PASSWORD_FIELD));
        assertThat(metaFields, hasKey(EMAIL_FIELD));
        assertThat(metaFields, hasKey(PERSON_FIELD));
        assertThat(metaFields, hasKey(COUNTRY_FIELD));
        assertThat(metaFields, hasKey(ORDERS_FIELD));

    }

    @Test
    void assertThatMetaFieldsHaveRightTypes() {
        Map<Field, AbstractMetaField> metaFields = userMetaData.getMetaFields();
        assertThat(metaFields.get(USERNAME_FIELD), instanceOf(InternalMetaField.class));
        assertThat(metaFields.get(PASSWORD_FIELD), instanceOf(InternalMetaField.class));
        assertThat(metaFields.get(EMAIL_FIELD), instanceOf(InternalMetaField.class));
        assertThat(metaFields.get(PERSON_FIELD), instanceOf(ExternalMetaField.class));
        assertThat(metaFields.get(COUNTRY_FIELD), instanceOf(ExternalMetaField.class));
        assertThat(metaFields.get(ORDERS_FIELD), instanceOf(CollectionMetaField.class));
    }

    @Test
    void assertThatMetaFieldsHaveRightFieldTypes() {
        Map<Field, AbstractMetaField> metaFields = userMetaData.getMetaFields();
        assertThat(((InternalMetaField)metaFields.get(USERNAME_FIELD)).getDbFieldName(), is(USERNAME_FIELD_DB_NAME));
        assertThat(metaFields.get(COUNTRY_FIELD).getFieldType(), is(TYPE_OF_COUNTRY_FIELD));
        assertThat(metaFields.get(ORDERS_FIELD).getFieldType(), is(TYPE_OF_ORDERS_FIELD));
        assertThat(((CollectionMetaField)metaFields.get(ORDERS_FIELD)).getGenericType(), is(GENERIC_TYPE_OF_ORDERS_FIELD));
    }
}