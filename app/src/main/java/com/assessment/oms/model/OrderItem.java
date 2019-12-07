package com.assessment.oms.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Entity(name = "TBL_ORDER_ITEM")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class OrderItem implements Serializable {

    private static final String colID = "ID";
    private static final String colQty = "ORDER_QUANTITY";
    private static final String colCreatedAt = "CREATED_AT";
    private static final String colUpdatedAt = "UPDATED_AT";
    private static final String colOrderProductID = "FK_PRODUCT_ID";
    private static final String colProductPrice = "PRODUCT_PRICE";
    private static final String colFKOrder = "FK_ORDER_ID";
    private static final String colDeleted = "DELETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = colID)
    private Long id;

    @Column(name = colQty, nullable = false)
    private Integer quantityOrdered;

    @Column(name = colProductPrice, nullable = false)
    private BigDecimal productPriceAtOrder;

    @OneToOne
    @JoinColumn(name = colOrderProductID)
    private Product product;

    @Column(name = colCreatedAt)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = colUpdatedAt)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = colFKOrder)
    @JsonIgnore
    private Order order;

    @Column(name = colDeleted, nullable = false, columnDefinition = "boolean default false")
    @JsonIgnore
    private boolean deleted;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", quantityOrdered=" + quantityOrdered +
                ", productPriceAtOrder=" + productPriceAtOrder +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", order=" + order +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) &&
                Objects.equals(quantityOrdered, orderItem.quantityOrdered) &&
                Objects.equals(productPriceAtOrder, orderItem.productPriceAtOrder) &&
                Objects.equals(product, orderItem.product) &&
                Objects.equals(createdAt, orderItem.createdAt) &&
                Objects.equals(updatedAt, orderItem.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, quantityOrdered, productPriceAtOrder, product, createdAt, updatedAt);
    }
}
