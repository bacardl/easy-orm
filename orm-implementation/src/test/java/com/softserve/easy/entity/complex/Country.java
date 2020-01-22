package com.softserve.easy.entity.complex;

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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Country)) return false;
        Country country = (Country) object;
        return Objects.equals(getName(), country.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Country.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .toString();
    }
}
