package com.softserve.easy.entity;

import com.softserve.easy.annotation.Entity;

@Entity
public class User {
    private long id;
    private String name;
    private int age;

    public User(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public User(String s, int i) {
        this.name = s;
        this.age = i;
    }
}
