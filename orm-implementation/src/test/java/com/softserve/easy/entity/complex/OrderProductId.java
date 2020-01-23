package com.softserve.easy.entity.complex;

import com.softserve.easy.annotation.Column;
import com.softserve.easy.annotation.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

@Embeddable
public class OrderProductId implements Serializable {
    private static final long serialVersionUID = -7353289935705585165L;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "product_id")
    private Long productId;

    public OrderProductId() {
    }

    public OrderProductId(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrderProductId.class.getSimpleName() + "[", "]")
                .add("orderId=" + orderId)
                .add("productId=" + productId)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderProductId)) return false;
        OrderProductId that = (OrderProductId) o;
        return Objects.equals(getOrderId(),that.getOrderId()) &&
                Objects.equals(getProductId(), that.getProductId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrderId(), getProductId());
    }
}
