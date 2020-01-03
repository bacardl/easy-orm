package com.softserve.easy.core;

public interface SessionFactory {
    Session openSession();
    Session getCurrentSession();
    boolean isClosed();
    void close();
}
