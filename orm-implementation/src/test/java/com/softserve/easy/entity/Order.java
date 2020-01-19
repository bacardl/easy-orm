package com.softserve.easy.entity;

import com.softserve.easy.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "Order")
@Table(name = "orders")
public class Order {
    @Id
    // @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    @ManyToOne
    private User user;
    private String status;
    private Date createdAt;

    //    @OneToMany(
//            mappedBy = "orders",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true
//    )
    private List<OrderProduct> orderProducts;

    public Order() {
    }

    public Order(User user, String status, Date createdAt) {
        this.user = user;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(getUser(), order.getUser()) &&
                Objects.equals(getStatus(), order.getStatus()) &&
                Objects.equals(getCreatedAt(), order.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getStatus(), getCreatedAt());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("user=" + user)
                .add("status='" + status + "'")
                .add("createdAt=" + createdAt)
                .add("products=" + orderProducts)
                .toString();
    }
}
