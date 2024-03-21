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
    @NotBlank(message = "số điện thoại không được để trống")
    @Size(min =7,message = "độ dài số điện thoại quá ngắn")
    private String phoneNumber;

    private String email;

    @NotBlank(message = "địa chỉ không được để trống")
    private String address;

    private String note;

    @JsonProperty("user_id")
    @Min(value =1,message = "userID phải >0")
    private Long userId;

    @JsonProperty("total_money")
    @Min(value = 0,message = "tổng tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal totalMoney;

    @JsonProperty("shipping_method")
    private String shippingMethod;

    @JsonProperty("payment_Method")
    private String paymentMethod;

    @JsonProperty("shipping_date")
    private LocalDate shippingDate;

    @JsonProperty("shipping_address")
    private String shippingAddress;
}