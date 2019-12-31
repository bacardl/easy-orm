package com.softserve.easy.entity;

import com.softserve.easy.annotation.Entity;

import java.util.StringJoiner;

@Entity(name = "Product")
// @Table(name = "products")
public class Product {
    // @Id
    // @GeneratedValue
    private Long id;
    private String name;
    private Double price;
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("price=" + price)
                .add("description='" + description + "'")
                .toString();
    }
}
