package com.softserve.easy.meta;

public interface Retrievable<T> {
    T retrieveValue(Object object) throws IllegalAccessException;
}
