package com.project.shopapp.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.shopapp.models.Cart;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("quantity")
    private int quantity;

    public static CartResponse fromCart(Cart cart){
        return CartResponse.builder()
               .id(cart.getId())
               .userId(cart.getUser().getId())
               .productId(cart.getProduct().getId())
               .quantity(cart.getQuantity())
               .build();
    }
}
