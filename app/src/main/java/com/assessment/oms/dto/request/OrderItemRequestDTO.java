package com.assessment.oms.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Order Item")
public class OrderItemRequestDTO {

    @ApiModelProperty(notes = "Product Id", required = true)
    private Long productId;

    @ApiModelProperty(notes = "Order Item Quantity", required = true)
    private Integer quantity;
}
