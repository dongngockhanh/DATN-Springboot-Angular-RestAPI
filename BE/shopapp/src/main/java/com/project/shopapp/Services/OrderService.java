package com.project.shopapp.Services;

import com.project.shopapp.DTOs.OrderDTO;
import com.project.shopapp.DTOs.responses.OrderResponse;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    OrderResponse getOrderById(long id) throws DataNotFoundException, Exception;
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrderByUserId(long userId) throws DataNotFoundException, Exception;
    OrderResponse updateOrder(long orderId, OrderDTO orderDTO) throws Exception;
    void setStatusOrder(Long orderId) throws Exception;
    void deleteOrder(long id) throws Exception;
}
