package com.project.shopapp.Services;

import com.project.shopapp.DTOs.OrderDTO;
import com.project.shopapp.DTOs.responses.OrderResponse;
import com.project.shopapp.exceptions.DataNotFoundException;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;
    OrderResponse getOrderById(long id) throws DataNotFoundException, Exception;
    List<OrderResponse> getAllOrders(long userId) throws DataNotFoundException, Exception;
    OrderResponse updateOrder(long orderId, OrderDTO orderDTO) throws Exception;
    void deleteOrder(long id) throws Exception;
}
