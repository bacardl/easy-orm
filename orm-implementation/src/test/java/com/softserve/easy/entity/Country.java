package com.softserve.easy.entity;

import com.softserve.easy.annotation.Entity;

import java.util.StringJoiner;

@Entity(name = "Country")
// @Table(name = "countries")
public class Country {

    // @Id
    // @GeneratedValue
    // @Column(name = "code")
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
