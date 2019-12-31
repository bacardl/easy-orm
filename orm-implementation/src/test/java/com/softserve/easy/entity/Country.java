package com.softserve.easy.entity;

import java.util.StringJoiner;

// @Entity(name = "Country")
// @Table(name = "countries")
public class Country {

    // @Id
    // @GeneratedValue
    // @Column(name = "code")
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Country.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}
