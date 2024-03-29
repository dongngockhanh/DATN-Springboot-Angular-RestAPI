package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value=1,message = "order id  must be greater than or equal to 1")
    private Long orderId;

    @Min(value =1,message = "Product id must be greater than or equal to 1")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 1,message = "The quantity must be greater than or equal to 1")
    private int quantity;

//    @Min(value = 0,message = "price phải lớn hơn hoặc bằng 0")
//    private BigDecimal price;

//    @Min(value = 0,message = "total money phải lón hơn hoặc bằng 0")
//    @JsonProperty("total_money")
//    private BigDecimal totalMoney;

    private String color;
}
