package com.assessment.oms.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity(name = "TBL_ORDER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(description = "Order Entity.")
public class Order implements Serializable {

    private static final String colID = "ID";
    private static final String colBuyerEmail = "BUYER_EMAIL";
    private static final String colOrderDate = "ORDER_DATE";
    private static final String colOrderValue = "ORDER_VALUE";
    private static final String colFKOrder = "FK_ORDER_ID";
    private static final String colCreatedAt = "CREATED_AT";
    private static final String colUpdatedAt = "UPDATED_AT";
    private static final String colDeleted = "DELETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = colID)
    @ApiModelProperty(notes = "The database generated ID")
    private Long id;

    @Column(name = colBuyerEmail)
    @ApiModelProperty(notes = "Buyer's Email")
    private String buyerEmail;

    @Column(name = colOrderDate)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(notes = "The date of an order")
    private Date dateOfOrder;

    @Column(precision = 13, scale = 4, name = colOrderValue)
    @ApiModelProperty(notes = "Order Value")
    private BigDecimal orderValue;

    /**
     * Lazy initialization has been used to improve performance, avoid wasteful computation, and reduce program memory requirements
     */
    @OneToMany(cascade = { CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = colFKOrder)
    @JsonIgnore
    private Set<OrderItem> orderItems;

    @Column(name = colCreatedAt)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = colUpdatedAt)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = colDeleted, nullable = false, columnDefinition = "boolean default false")
    @JsonIgnore
    private boolean deleted;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", buyerEmail='" + buyerEmail + '\'' +
                ", dateOfOrder=" + dateOfOrder +
                ", orderValue=" + orderValue +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(buyerEmail, order.buyerEmail) &&
                Objects.equals(dateOfOrder, order.dateOfOrder) &&
                Objects.equals(orderValue, order.orderValue) &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, buyerEmail, dateOfOrder, orderValue, createdAt, updatedAt);
    }
}
