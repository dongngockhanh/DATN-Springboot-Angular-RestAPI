package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.OrderDetailDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.OrderDetailResponse;
import com.project.shopapp.Services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${api.basePath}/order_details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final MessageResponse messageResponse;

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.createOrderDetail(orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getAllDetails(@Valid @PathVariable("order_id") long order_id)
    {
        try {
            List<OrderDetailResponse> orderDetailResponses = orderDetailService.getAllDetails(order_id);
            return ResponseEntity.ok(orderDetailResponses);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetailById(@Valid @PathVariable long id){
        try {
            return ResponseEntity.ok(orderDetailService.getById(id));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable long id,@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
        try {
            OrderDetailResponse orderDetailResponse = orderDetailService.updateOrderDetail(id,orderDetailDTO);
            return ResponseEntity.ok(orderDetailResponse);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(@Valid @PathVariable long id)
    {
        try {
            orderDetailService.deleteOrderDetail(id);
            return ResponseEntity.ok("delete order detail successfully with id = "+id);
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().body(messageResponse);
        }
    }
}
