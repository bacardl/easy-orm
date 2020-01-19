package com.softserve.easy.meta;

import com.softserve.easy.entity.Country;
import com.softserve.easy.entity.Person;
import com.softserve.easy.entity.User;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class DependencyGraphTest {
    private static final Class<User> USER_CLASS = User.class;
    private static final Class<Person> PERSON_CLASS = Person.class;
    private static final Class<Country> COUNTRY_CLASS = Country.class;
    private static final int NUMBER_OF_ALL_CLASSES = 3;
    private static final Logger LOG = LoggerFactory.getLogger(DependencyGraphTest.class);

    private Set<Class<?>> classes;
    private DependencyGraph dependencyGraph;

    @BeforeEach
    public void init() {
        this.classes = new HashSet<>();
        classes.add(USER_CLASS);
        classes.add(PERSON_CLASS);
        classes.add(COUNTRY_CLASS);
        this.dependencyGraph = new DependencyGraph(classes);
    }


    @Test
    public void checkNumberOfAllInstances() {
        assertThat(dependencyGraph.getNumberOfAllClasses(), equalTo(NUMBER_OF_ALL_CLASSES));
    }

    @Test
    public void checkExplicitDependencies() {
        assertThat(dependencyGraph.getExplicitDependencies(USER_CLASS), not(empty()));
        checkExplicitDependenciesByClass(dependencyGraph, USER_CLASS, PERSON_CLASS, COUNTRY_CLASS);
        checkExplicitDependenciesByClass(dependencyGraph, PERSON_CLASS, USER_CLASS);
        checkExplicitDependenciesByClass(dependencyGraph, COUNTRY_CLASS);
    }

    @Test
    public void checkImplicitDependencies() {
        assertThat(dependencyGraph.getAllDependencies(USER_CLASS), not(empty()));
        checkAllDependenciesByClass(dependencyGraph, USER_CLASS, PERSON_CLASS, COUNTRY_CLASS);
        checkAllDependenciesByClass(dependencyGraph, PERSON_CLASS, USER_CLASS, COUNTRY_CLASS);
        checkAllDependenciesByClass(dependencyGraph, COUNTRY_CLASS);
    }

    private static void checkExplicitDependenciesByClass(DependencyGraph classGraph,
                                                         Class<?> checkedClass, Class<?>... connectedClasses) {
        Set<Class<?>> verticesByClass = classGraph.getExplicitDependencies(checkedClass);
        assertThat(verticesByClass, Matchers.notNullValue());
        assertThat(checkedClass.getSimpleName()
                        + "'s vertices list should contain "
                        + connectedClasses.length
                        + " classes. Checking without order.",
                verticesByClass,
                containsInAnyOrder(connectedClasses)
        );
    }

    private static void checkAllDependenciesByClass(DependencyGraph classGraph,
                                                    Class<?> checkedClass, Class<?>... connectedClasses) {
        Set<Class<?>> verticesByClass = classGraph.getAllDependencies(checkedClass);
        assertThat(verticesByClass, Matchers.notNullValue());
        assertThat(checkedClass.getSimpleName()
                        + "'s vertices list should contain "
                        + connectedClasses.length
                        + " classes. Checking without order.",
                verticesByClass,
                containsInAnyOrder(connectedClasses)
        );
    }
}
