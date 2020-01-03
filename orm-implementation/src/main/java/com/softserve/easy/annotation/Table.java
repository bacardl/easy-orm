package com.softserve.easy.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Table {
    String name();
    String appliesTo();
    String comment() default "";
//    ForeignKey foreignKey() default @ForeignKey( name="" );
//    FetchMode fetch() default FetchMode.JOIN;
}
