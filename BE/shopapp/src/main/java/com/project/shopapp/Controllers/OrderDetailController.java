package com.project.shopapp.Controllers;

import com.project.shopapp.DTOs.OrderDetailDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.basePath}/order_details")
public class OrderDetailController {
    @PostMapping("")
    public ResponseEntity<?> insertOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO)
    {
        try {
            return ResponseEntity.ok("thêm mới thành công");
        }catch (Exception e)
        {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
