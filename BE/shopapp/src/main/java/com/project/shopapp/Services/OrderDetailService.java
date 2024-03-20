package com.project.shopapp.Services;

import com.project.shopapp.DTOs.OrderDetailDTO;
import com.project.shopapp.DTOs.responses.OrderDetailResponse;
import com.project.shopapp.models.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO);
    List<OrderDetailResponse> getAllDetails(long orderId);
    OrderDetailResponse getById(long id);
    OrderDetailResponse updateOrderDetail(long orderDetailId,OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(long id);
}
