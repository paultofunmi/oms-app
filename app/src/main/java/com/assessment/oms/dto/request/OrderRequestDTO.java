package com.assessment.oms.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Order Entity DTO.")
public class OrderRequestDTO {

    @ApiModelProperty(notes = "Buyer's email", required = true)
    private String buyerEmail;

    @ApiModelProperty(notes = "Date of order", hidden = true)
    private Date dateOfOrder;

    @ApiModelProperty(notes = "Total order value", hidden = true)
    private BigDecimal orderValue;

    @ApiModelProperty(notes = "Order items", required = true)
    private List<OrderItemRequestDTO> orderItems;
}
