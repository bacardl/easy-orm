package com.softserve.easy.core;

public interface Transaction {
    void commit();
    void rollback();
}
