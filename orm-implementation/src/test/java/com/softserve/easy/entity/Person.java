package com.softserve.easy.entity;

import com.softserve.easy.annotation.Entity;

import java.util.Date;
import java.util.StringJoiner;

@Entity(name = "Person")
// @Table(name = "persons")
// @GenericGenerator(name = "custom-generator", strategy = "foreign",
// parameters = { @Parameter(name = "property", value = "user")})
public class Person {

    // @Id
    // @GeneratedValue(generator = "custom-generator")
    private Long id;

    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    // @OneToOne
    // PrimaryKeyJoinColumn
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Person.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstName='" + firstName + "'")
                .add("lastName='" + lastName + "'")
                .add("dateOfBirth=" + dateOfBirth)
                .toString();
    }
}
