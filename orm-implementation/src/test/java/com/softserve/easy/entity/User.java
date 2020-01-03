package com.softserve.easy.entity;

import com.softserve.easy.annotation.Entity;

import java.util.Set;
import java.util.StringJoiner;

@Entity(name = "User")
// @Table(name = "users")
public class User {
    // TODO: resolve @OneToOne relation to Person entity

    // @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String email;

    // OneToOne
    // PrimaryKeyJoinColumn
    private Person person;

    // @ManyToOne
    private Country country;

    // @OneToMany
    private Set<Order> orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("password='" + password + "'")
                .add("email='" + email + "'")
                .add("country=" + country)
                .toString();
    }
}
