package com.softserve.easy.entity.complex;

import com.softserve.easy.annotation.*;
import com.softserve.easy.constant.FetchType;

import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "OrderProduct")
@Table(name = "order_items")
public class OrderProduct {

    @EmbeddedId
    private OrderProductId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("product_id")
    private Product product;

    private Integer quantity;

    public OrderProduct(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.id = new OrderProductId(order.getId(), product.getId());
    }

    public OrderProductId getId() {
        return id;
    }

    public void setId(OrderProductId id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderProduct.class.getSimpleName() + "[", "]")
                .add("order=" + order)
                .add("product=" + product)
                .add("quantity=" + quantity)
                .toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof OrderProduct)) return false;

        OrderProduct that = (OrderProduct) object;

        if (!getId().equals(that.getId())) return false;
        return getQuantity().equals(that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrder(), getProduct());
    }
}
