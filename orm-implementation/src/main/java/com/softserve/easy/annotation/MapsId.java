package com.softserve.easy.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface MapsId {
    /**
     * The name of the attribute within the composite key
     * to which the relationship attribute corresponds.
     */
    String value();
}
