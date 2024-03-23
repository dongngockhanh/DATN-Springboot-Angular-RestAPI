package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.OrderDTO;
import com.project.shopapp.DTOs.responses.OrderResponse;
import com.project.shopapp.Services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.basePath}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
    ) {
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = orderService.createOrder(orderDTO);
            return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@Valid @PathVariable("id") Long id)
    {
        try {
            OrderResponse orderById =  orderService.getOrderById(id);
            return ResponseEntity.ok(orderById);
        } catch (Exception e) {
             return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // lấy order theo user_id
    @GetMapping("/user/{user_id}")
    // GET http://localhost:8888/api/v1/orders/user/4
    public ResponseEntity<?> getOrderByUserId(@Valid @PathVariable("user_id") Long userId)
    {
        try {
            List<OrderResponse> ordersByUserIdResponse  = orderService.getAllOrders(userId);
            return ResponseEntity.ok().body(ordersByUserIdResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    // PUT http://localhost:8888/api/v1/orders/2
    public ResponseEntity<?> updateOrderById(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody OrderDTO orderDTO,BindingResult result)
    {
        try {
            if(result.hasErrors())
            {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            OrderResponse orderResponse = orderService.updateOrder(id,orderDTO);
            return ResponseEntity.ok(orderResponse);
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    // DELETE http://localhost:8888/api/v1/orders/2
    public ResponseEntity<?> deleteOrderById(@Valid @PathVariable("id") Long id)
    {
        try {
            orderService.deleteOrder(id);
            // xoá mềm => cập nhật truòng active = false
            return ResponseEntity.ok().body(String.format("Delete order with id=%d successfully",id));
        } catch (Exception e) {
           return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
