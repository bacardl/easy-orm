package com.softserve.easy.client.entity;

import com.google.common.base.MoreObjects;
import com.softserve.easy.annotation.*;

import java.util.Objects;
import java.util.Set;

@Entity(name = "User")
@Table(name = "users")
public class User {

    @Id @GeneratedValue
    @Column(name = "person_id")
    private Long id;

    private String username;
    private String password;
    private String email;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "country_code")
    private Country country;

    @OneToMany
    private Set<Order> orders;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof User)) return false;
        User user = (User) object;
        return getUsername().equals(user.getUsername()) &&
                getPassword().equals(user.getPassword()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), getEmail());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("username", username)
                .add("password", password)
                .add("email", email)
                .add("person", person)
                .add("country", country)
                .add("orders", orders)
                .toString();
    }
}
