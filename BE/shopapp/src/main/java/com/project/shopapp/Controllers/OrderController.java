package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.OrderDTO;
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
public class OrderController {
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
            return ResponseEntity.status(HttpStatus.OK).body("tạo đơn hàng thành công");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{user_id}")
    // GET http://localhost:8888/api/v1/orders/4
    public ResponseEntity<?> getOrderByUserId(@Valid @PathVariable("user_id") Long userId)
    {
        try {
            return ResponseEntity.ok().body("lấy danh sách order theo id");
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
            @Valid @RequestBody OrderDTO orderDTO)
    {

            return ResponseEntity.ok().body("cập nhật order 1 order");
    }

    @DeleteMapping("/{id}")
    // DELETE http://localhost:8888/api/v1/orders/2
    public ResponseEntity<?> deleteOrderById(@Valid @PathVariable("id") Long id)
    {
        // xoá mềm => cập nhật truòng active = false
            return ResponseEntity.ok().body("xoá order 1 order");
    }
}
