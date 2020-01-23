package com.softserve.easy.client.entity;

import com.softserve.easy.annotation.*;

import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id
    private Long id;

    @Column(name = "username")
    private String username;
    private String password;
    private String email;

    @ManyToOne
    @Column(name = "country_code")
    private Country country;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                getUsername().equals(user.getUsername()) &&
                getPassword().equals(user.getPassword()) &&
                getEmail().equals(user.getEmail()) &&
                Objects.equals(getCountry(), user.getCountry());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getEmail(), getCountry());
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
