package com.project.shopapp.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number can not be left blank")
    @Size(min =7,message = "Phone number length is too short")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    private String note;

    @JsonProperty("user_id")
    @Min(value =1,message = "userID must be >0")
    private Long userId;

    @JsonProperty("total_money")
    @Min(value = 0,message = "The total amount must be greater than or equal to 0")
    private BigDecimal totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_address")
    private String shippingAddress;

    @JsonProperty("cart_items")
    private List<CartItemDTO> cartItemDTOS;

}
