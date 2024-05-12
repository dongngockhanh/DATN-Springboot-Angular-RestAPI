package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.CartItemDTO;
import com.project.shopapp.DTOs.responses.CartResponse;
import com.project.shopapp.Repositories.CartRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Repositories.UserRepository;
import com.project.shopapp.Services.CartService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Cart;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public List<CartResponse> getCartByUser(Long userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);
        List<CartResponse> cartResponses = new ArrayList<>();
        for(Cart cart:carts){
            cartResponses.add(CartResponse.fromCart(cart));
        }
        return cartResponses;
    }

    @Override
    public CartResponse createAndUpdateCart(CartItemDTO cartItemDTO) {
        User user = userRepository.findById(cartItemDTO.getUserId()).orElseThrow(()->new DataNotFoundException("Not found user"));
        Product product = productRepository.findById(cartItemDTO.getProductId()).orElseThrow(()->new DataNotFoundException("Not found product"));
        List<Cart> listCart = cartRepository.findByUserId(cartItemDTO.getUserId());
        if(!listCart.isEmpty()){
            for(Cart cart : listCart){
                if(cart.getProduct().getId().equals(cartItemDTO.getProductId())){
                    cart.setQuantity(cartItemDTO.getQuantity());
                    cartRepository.save(cart);
                    return CartResponse.fromCart(cart);
                }
            }
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setProduct(product);
        cart.setQuantity(cartItemDTO.getQuantity());
        cartRepository.save(cart);
        return CartResponse.fromCart(cart);
    }

    @Override
    public void deleteCart(Long userId,Long productId) {
        List<Cart> cartOfUser = cartRepository.findByUserId(userId);
        for (Cart cart: cartOfUser) {
            if (cart.getProduct().getId().equals(productId)) {
                cartRepository.delete(cart);
                break;
            }
        }
    }
}
