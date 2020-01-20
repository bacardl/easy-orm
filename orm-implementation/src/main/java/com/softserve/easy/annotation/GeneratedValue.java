package com.softserve.easy.annotation;


import com.softserve.easy.constant.GenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static com.softserve.easy.constant.GenerationType.AUTO;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
public @interface GeneratedValue {
    GenerationType strategy() default AUTO;
    String generator() default "";
}
