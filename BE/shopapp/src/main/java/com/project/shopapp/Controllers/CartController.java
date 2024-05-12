package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.CartItemDTO;
import com.project.shopapp.Services.CartService;
import com.project.shopapp.models.Cart;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.basePath}/carts")
public class CartController {
    private final CartService cartService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getCartByUser(@PathVariable("id")Long id){
        try {
            return ResponseEntity.ok(cartService.getCartByUser(id));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> createProductOfCart(@RequestBody CartItemDTO cartItemDTO){
        try {
            return ResponseEntity.ok(cartService.createAndUpdateCart(cartItemDTO));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<?> deleteProductOfCart(@PathVariable Long userId, @PathVariable Long productId){
        try {
            cartService.deleteCart(userId,productId);
            return ResponseEntity.ok().body("xoá thành công khỏi giỏ hàng");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
