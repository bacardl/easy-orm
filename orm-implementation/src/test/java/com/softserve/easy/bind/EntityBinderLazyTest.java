package com.softserve.easy.bind;

import com.softserve.easy.QueryConstant;
import com.softserve.easy.SimpleDbUnitTest;
import com.softserve.easy.cfg.Configuration;
import com.softserve.easy.entity.lazy.User;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.jdbc.Persister;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

import static com.softserve.easy.SimpleTestEnvironment.COUNTRY_CLASS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(MockitoJUnitRunner.class)
public class EntityBinderLazyTest extends SimpleDbUnitTest {
    private static final Class<User> LAZY_USER_CLASS = User.class;
    private static final Class<Country> LAZY_COUNTRY_CLASS = Country.class;
    @Mock
    private Persister persister;

    @InjectMocks
    private EntityBinderImpl entityBinder;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(LAZY_USER_CLASS);
        configuration.addAnnotatedClass(LAZY_COUNTRY_CLASS);
        entityBinder = new EntityBinderImpl(configuration.getMetaContext(), persister);
    }
    @Test
    public void shouldBuildUserProxyInstance() throws IllegalAccessException, InstantiationException, NoSuchFieldException {
//        MetaContext metaContext = getConfiguration().getMetaContext();
//        MetaData userMeta = metaContext.getMetaDataMap().get(USER_CLASS);
//        List<ExternalMetaField> externalMetaField = userMeta.getExternalMetaField();
//        Class<User> subjectClass = USER_CLASS;
//
//        DynamicType.Builder<?> subclass = new ByteBuddy().subclass(subjectClass);
////
//        for (ExternalMetaField metaField : externalMetaField) {
//            Optional<Method> getterForField = ClassScanner.getGetterForField(subjectClass, metaField.getField());
//            if (getterForField.isPresent()) {
//                Method getter = getterForField.get();
//                subclass = subclass.method(ElementMatchers.anyOf(getter))
//                        .intercept(MethodDelegation
//                                .to(new LazyLoadingInterceptor())
//                                    .andThen(SuperMethodCall.INSTANCE));
//            }
//        }
//
//        Class<?> proxyClass = subclass.make().load(getClass().getClassLoader()).getLoaded();
//
//        User user = (User) proxyClass.newInstance();
//        user.getCountry();
//
//        Country country = new Country();
//        country.setId(1);
//        country.setName("USA");
//
//        user.setCountry(country);
//
//        Country sameCountry = user.getCountry();
//        System.out.println(sameCountry);
    }
    @Test
    public void assertGetLazyEntityByIdHasBeenCalled() throws Exception {
        Country country = new Country();
        country.setId(100);
        country.setName("USA");
        Mockito.when(persister.getLazyEntityById(COUNTRY_CLASS, 100)).thenReturn(country);

        Connection connection = getClientConnection();
        PreparedStatement preparedStatement
                = connection.prepareStatement(QueryConstant.SELECT_USER_BY_ID_1);

        ResultSet resultSet = preparedStatement.executeQuery();

        // important!
        resultSet.next();

        Optional<User> optionalUser = entityBinder.buildEntity(LAZY_USER_CLASS, resultSet);
        User actualUser = optionalUser.orElseGet(Assertions::fail);
        Country actualCountry = actualUser.getCountry();
        assertThat(actualCountry, notNullValue());
        Mockito.verify(persister).getLazyEntityById(LAZY_COUNTRY_CLASS, 100);

    }
}