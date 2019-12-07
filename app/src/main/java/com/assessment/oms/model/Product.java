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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Entity(name = "TBL_PRODUCT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ApiModel(description = "Product Entity.")
@SQLDelete(sql = "UPDATE tbl_product " + "SET deleted = true " + "WHERE id = ?")
@Where(clause = "deleted = false")
public class Product {

    private static final String colProductName = "PRODUCT_NAME";
    private static final String colProductID = "ID";
    private static final String colProductPrice = "PRODUCT_PRICE";
    private static final String colCreatedAt = "CREATED_AT";
    private static final String colUpdatedAt = "UPDATED_AT";
    private static final String colDeleted = "DELETED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = colProductID)
    @ApiModelProperty(notes = "Product Id", hidden = true)
    private Long id;

    @Column(name = colProductName)
    @ApiModelProperty(notes = "Product Name", required = true)
    private String productName;

    @Column(precision = 13, scale = 4)
    @ApiModelProperty(notes = "Product price", required =  true)
    private BigDecimal productPrice;

    @Column(name = colCreatedAt)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(hidden = true)
    private Date createdAt;

    @Column(name = colUpdatedAt)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(hidden = true)
    @UpdateTimestamp
    private Date updatedAt;

    @Column(name = colDeleted, nullable = false, columnDefinition = "boolean default false")
    @JsonIgnore
    private boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(productName, product.productName) &&
                Objects.equals(productPrice, product.productPrice) &&
                Objects.equals(createdAt, product.createdAt) &&
                Objects.equals(updatedAt, product.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, productPrice, createdAt, updatedAt);
    }
}
