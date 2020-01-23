package com.softserve.easy.entity.complex;

import com.softserve.easy.annotation.Entity;
import com.softserve.easy.annotation.Id;
import com.softserve.easy.annotation.Table;

import java.util.StringJoiner;

@Entity(name = "Product")
@Table(name = "products")
public class Product {
    @Id
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Product)) return false;

        Product product = (Product) object;

        if (!getName().equals(product.getName())) return false;
        if (!getPrice().equals(product.getPrice())) return false;
        return getDescription().equals(product.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getPrice().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
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
