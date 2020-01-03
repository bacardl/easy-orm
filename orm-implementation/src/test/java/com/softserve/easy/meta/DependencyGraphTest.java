package com.softserve.easy.meta;

import com.softserve.easy.entity.Country;
import com.softserve.easy.entity.Order;
import com.softserve.easy.entity.Person;
import com.softserve.easy.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class DependencyGraphTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final Class<Person> PERSON_CLASS = Person.class;
    private static final Class<Country> COUNTRY_CLASS = Country.class;
    private static final int NUMBER_OF_ALL_CLASSES = 4;

    private Set<Class<?>> classes;
    private DependencyGraph dependencyGraph;

    @BeforeEach
    void init() {
        this.classes = new HashSet<>();
        classes.add(USER_CLASS);
        classes.add(PERSON_CLASS);
        classes.add(COUNTRY_CLASS);
        this.dependencyGraph = new DependencyGraph(classes);
    }

    @Test
    void buildDependencyGraphOfInitClasses() {
        Assertions.assertFalse(dependencyGraph.getExplicitDependencies(USER_CLASS).isEmpty());
        Assertions.assertEquals(NUMBER_OF_ALL_CLASSES, dependencyGraph.getNumberOfAllClasses());

        checkVerticesByClass(dependencyGraph, USER_CLASS, Person.class, Order.class, Country.class);
        checkVerticesByClass(dependencyGraph, Person.class, USER_CLASS);
        checkVerticesByClass(dependencyGraph, Country.class);
    }

    private static void checkVerticesByClass(DependencyGraph classGraph,
                                             Class<?> checkedClass, Class<?>... connectedClasses) {
        Set<Class<?>> userVertices = classGraph.getExplicitDependencies(checkedClass);
        Assertions.assertNotNull(userVertices);
        assertThat(checkedClass.getSimpleName() + "'s vertices list should contain 3 specific classes. Checking without order.",
                userVertices,
                containsInAnyOrder(connectedClasses)
        );
    }
}
