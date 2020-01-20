package com.softserve.easy.helper;

import com.softserve.easy.annotation.Entity;
import com.softserve.easy.entity.complex.Order;
import com.softserve.easy.entity.complex.OrderProduct;
import com.softserve.easy.entity.complex.Person;
import com.softserve.easy.entity.complex.Product;
import com.softserve.easy.entity.simple.Country;
import com.softserve.easy.entity.simple.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import static com.softserve.easy.SimpleTestEnvironment.USER_CLASS;
import static org.junit.Assert.assertThat;

class ClassScannerTest {
    @Test
    void getAnnotatedClassesFromSimpleEntityPackage() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class,
                "com.softserve.easy.simpleEntity");

        assertThat(annotatedClasses, Matchers.hasSize(2));
        assertThat(annotatedClasses,
                Matchers.containsInAnyOrder(
                        User.class,
                        Country.class));
    }

    @Test
    void getAnnotatedClassesFromComplexEntityPackage() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class,
                "com.softserve.easy.entity");

        assertThat(annotatedClasses, Matchers.hasSize(6));
        assertThat(annotatedClasses,
                Matchers.containsInAnyOrder(
                        com.softserve.easy.entity.complex.User.class,
                        Order.class,
                        OrderProduct.class,
//                        com.softserve.easy.entity.complex.OrderProductId.class, @Embeddable
                        com.softserve.easy.entity.complex.Country.class,
                        Person.class,
                        Product.class
                ));
    }

    /**
    depends on number of classes that annotated with @Entity located at classpath
     */
    @Test
    void getAnnotatedClassesFromClasspath() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class);
        assertThat(annotatedClasses, Matchers.hasSize(8));
        assertThat(annotatedClasses,
                Matchers.containsInAnyOrder(
                        User.class,
                        Country.class,
                        com.softserve.easy.entity.complex.User.class,
                        Order.class,
                        OrderProduct.class,
//                        com.softserve.easy.entity.complex.OrderProductId.class, @Embeddable
                        com.softserve.easy.entity.complex.Country.class,
                        Person.class,
                        Product.class
                ));
    }

    @Test
    void shouldFindCountryGetter() throws NoSuchFieldException {
        Field countryField = USER_CLASS.getDeclaredField("country");
        assertThat(ClassScanner.getGetterForField(USER_CLASS, countryField).isPresent(), Matchers.is(true));
        Method method = ClassScanner.getGetterForField(USER_CLASS, countryField).get();
        System.out.println(method.getName());
    }
}