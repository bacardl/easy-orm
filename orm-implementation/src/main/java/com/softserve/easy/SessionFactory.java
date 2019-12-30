package com.softserve.easy;

public interface SessionFactory {
    Session openSession();
    Session getCurrentSession();
    boolean isClosed();
    void close();
}
