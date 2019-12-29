package com.softserve.easy.helper;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassScanner {
    public static final Logger LOG = LoggerFactory.getLogger(ClassScanner.class);
    public static Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections("");
        Set<Class<?>> annotatedClassesList = reflections.getTypesAnnotatedWith(annotation);
        LOG.info("Found {} classes annotated with {}", annotatedClassesList.size(), annotation.toString());
        return annotatedClassesList;
    }
}
