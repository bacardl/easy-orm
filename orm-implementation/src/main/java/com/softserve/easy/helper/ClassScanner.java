package com.softserve.easy.helper;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.reflections.ReflectionUtils.*;

public class ClassScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);
    private static final String ALL_CLASS_PATH = "";

    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        return getAnnotatedClasses(annotation, ALL_CLASS_PATH);
    }

    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation, String place) {
        Reflections reflections = new Reflections(place);
        Set<Class<?>> annotatedClassesList = reflections.getTypesAnnotatedWith(annotation);
        LOG.info("Found {} classes annotated with {}", annotatedClassesList.size(), annotation.toString());
        return annotatedClassesList;
    }

    @SuppressWarnings("unchecked")
    public static Optional<Method> getGetterForField(Class<?> clazz, Field field) {
        LOG.debug("Try to get a getter method from the class {} for field {}", clazz.getSimpleName(), field.getName());
        String getterName = "get" + capitalizeFirstLetter(field.getName());
        Set<Method> allMethods = getAllMethods(clazz,
                withModifier(Modifier.PUBLIC),
                withName(getterName),
                withParametersCount(0));
        LOG.debug("It turned out to find {} methods", allMethods.size());
        if (allMethods.size() > 1) {
            throw new IllegalStateException("There are many declared getters for field.");
        }
        if (allMethods.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(allMethods.iterator().next());
    }

    private static String capitalizeFirstLetter(String str) {
        Objects.requireNonNull(str);
        if (str.isEmpty()) {
            throw new IllegalArgumentException("String " + str + " must be non empty.");
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
