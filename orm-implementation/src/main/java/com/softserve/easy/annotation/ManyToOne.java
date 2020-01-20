package com.softserve.easy.annotation;

import com.softserve.easy.constant.FetchType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface ManyToOne {
    FetchType fetch() default FetchType.EAGER;
}
