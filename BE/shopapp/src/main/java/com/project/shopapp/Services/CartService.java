package com.project.shopapp.Services;

import com.project.shopapp.DTOs.CartItemDTO;
import com.project.shopapp.DTOs.responses.CartResponse;
import com.project.shopapp.models.Cart;

import java.util.List;

public interface CartService {
    List<CartResponse> getCartByUser(Long userId);
    CartResponse createAndUpdateCart(CartItemDTO cartItemDTO);
    void deleteCart(Long userId,Long productId);
}
