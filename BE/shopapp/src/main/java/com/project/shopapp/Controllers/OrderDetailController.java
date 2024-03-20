package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.OrderDetailDTO;
import com.project.shopapp.DTOs.responses.OrderDetailResponse;
import com.project.shopapp.Services.OrderDetailService;
import com.project.shopapp.models.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.basePath}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
            OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<List<OrderDetailResponse>> getAllDetails(@Valid @PathVariable("order_id") long order_id)
    {
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.getAllDetails(order_id);
        return ResponseEntity.ok(orderDetailResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable long id){
        return ResponseEntity.ok(orderDetailService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable long id,@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
        OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id,orderDetailDTO);
        return ResponseEntity.ok(orderDetailResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable long id)
    {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("xoá thành công chi tiết đơn hàng với id = "+id);
    }
}
