package com.softserve.easy.meta;

public interface Injectable<T> {
    void injectValue(T value, Object object) throws IllegalAccessException ;
    boolean checkTypeCompatibility(T value);
}
