package com.softserve.easy.helper;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

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
}
