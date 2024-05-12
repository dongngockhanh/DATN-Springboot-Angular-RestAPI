package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

//@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends BaseResponse{
    private long id;

    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String email;
    private String address;
    private String note;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("total_money")
    private BigDecimal totalMoney;

    @JsonProperty("order_date")
    private Date orderDate;

    private String status;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("tracking_number")
    private String trackingNumber;

    @JsonProperty("shipping_address")
    private String shippingAddress;

}
