package com.softserve.easy.helper;

import com.softserve.easy.annotation.Entity;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.Assert.assertThat;

class ClassScannerTest {
    @Test
    void getAnnotatedClassesFromSimpleEntityPackage() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class,
                "com.softserve.easy.simpleEntity");

        assertThat(annotatedClasses, Matchers.hasSize(2));
        assertThat(annotatedClasses,
                Matchers.containsInAnyOrder(
                        com.softserve.easy.simpleEntity.User.class,
                        com.softserve.easy.simpleEntity.Country.class));
    }

    @Test
    void getAnnotatedClassesFromComplexEntityPackage() {
        Set<Class<?>> annotatedClasses = ClassScanner.getAnnotatedClasses(Entity.class,
                "com.softserve.easy.entity");

        assertThat(annotatedClasses, Matchers.hasSize(6));
        assertThat(annotatedClasses,
                Matchers.containsInAnyOrder(
                        com.softserve.easy.entity.User.class,
                        com.softserve.easy.entity.Order.class,
                        com.softserve.easy.entity.OrderProduct.class,
//                        com.softserve.easy.entity.OrderProductId.class, @Embeddable
                        com.softserve.easy.entity.Country.class,
                        com.softserve.easy.entity.Person.class,
                        com.softserve.easy.entity.Product.class
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
                        com.softserve.easy.simpleEntity.User.class,
                        com.softserve.easy.simpleEntity.Country.class,
                        com.softserve.easy.entity.User.class,
                        com.softserve.easy.entity.Order.class,
                        com.softserve.easy.entity.OrderProduct.class,
//                        com.softserve.easy.entity.OrderProductId.class, @Embeddable
                        com.softserve.easy.entity.Country.class,
                        com.softserve.easy.entity.Person.class,
                        com.softserve.easy.entity.Product.class
                ));
    }
}