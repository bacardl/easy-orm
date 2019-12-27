package com.softserve.orm.annotation;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface Table {
    String appliesTo();
    String comment() default "";
//    ForeignKey foreignKey() default @ForeignKey( name="" );
//    FetchMode fetch() default FetchMode.JOIN;
}
