package com.softserve.easy.bind;

import com.softserve.easy.QueryConstant;
import com.softserve.easy.SimpleDbUnitTest;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import com.softserve.easy.helper.ClassScanner;
import com.softserve.easy.meta.MetaContext;
import com.softserve.easy.meta.MetaData;
import com.softserve.easy.meta.field.ExternalMetaField;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import static com.softserve.easy.SimpleTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class EntityBinderImplSimpleTest extends SimpleDbUnitTest {
    private final EntityBinder entityBinder = new EntityBinderImpl(getConfiguration().getMetaContext());

    @Test
    public void shouldBuildUserWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_1);
        ResultSet resultSet = preparedStatement.executeQuery();
        // important!
        resultSet.next();

        Optional<User> optionalUser = entityBinder.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getId(), equalTo(REFERENCE_USER.getId()));
        assertThat(actualUser.getUsername(), equalTo(REFERENCE_USER.getUsername()));
        assertThat(actualUser.getPassword(), equalTo(REFERENCE_USER.getPassword()));
        assertThat(actualUser.getEmail(), equalTo(REFERENCE_USER.getEmail()));
    }

    @Test
    public void shouldBuildUserWithExternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_1);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = entityBinder.buildEntity(USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);

        assertThat(actualUser.getCountry(), notNullValue());

        assertThat(actualUser.getCountry().getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualUser.getCountry().getName(), equalTo(REFERENCE_COUNTRY.getName()));
    }

    @Test
    public void shouldBuildCountryWithInternalFields() throws Exception {
        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_COUNTRY_BY_ID);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<Country> optionalCountry = entityBinder.buildEntity(COUNTRY_CLASS, resultSet);
        Country actualCountry = optionalCountry.orElseGet(Assertions::fail);

        assertThat(actualCountry.getId(), equalTo(REFERENCE_COUNTRY.getId()));
        assertThat(actualCountry.getName(), equalTo(REFERENCE_COUNTRY.getName()));
    }

    @Test
    public void shouldBuildUserProxyInstance() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        MetaContext metaContext = getConfiguration().getMetaContext();
        MetaData userMeta = metaContext.getMetaDataMap().get(USER_CLASS);
        List<ExternalMetaField> externalMetaField = userMeta.getExternalMetaField();
        Class<User> subjectClass = USER_CLASS;

        DynamicType.Builder<?> subclass = new ByteBuddy().subclass(subjectClass);
//
        for (ExternalMetaField metaField : externalMetaField) {
            Optional<Method> getterForField = ClassScanner.getGetterForField(subjectClass, metaField.getField());
            if (getterForField.isPresent()) {
                Method getter = getterForField.get();
                subclass = subclass.method(ElementMatchers.anyOf(getter))
                        .intercept(MethodDelegation
                                .to(new LazyLoadingInterceptor())
                                    .andThen(SuperMethodCall.INSTANCE));
            }
        }

        Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();

        User user = (User) proxyClass.newInstance();
        user.getCountry();

        Country country = new Country();
        country.setId(1);
        country.setName("USA");

        user.setCountry(country);

        Country sameCountry = user.getCountry();
        System.out.println(sameCountry);
    }
}