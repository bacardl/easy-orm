package com.softserve.easy.entity.simple;

import com.softserve.easy.annotation.Column;
import com.softserve.easy.annotation.Entity;
import com.softserve.easy.annotation.Id;
import com.softserve.easy.annotation.Table;

import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "Country")
@Table(name = "countries")
public class Country {

    @Id
    // @GeneratedValue
    @Column(name = "code")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country)) return false;
        Country country = (Country) o;
        return Objects.equals(getId(), country.getId()) &&
                getName().equals(country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Country.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}
