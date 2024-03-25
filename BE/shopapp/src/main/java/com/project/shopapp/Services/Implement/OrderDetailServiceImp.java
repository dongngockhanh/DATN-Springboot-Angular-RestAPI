package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.OrderDetailDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.OrderDetailResponse;
import com.project.shopapp.Repositories.OrderDetailRepository;
import com.project.shopapp.Repositories.OrderRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Services.OrderDetailService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderDetail;
import com.project.shopapp.models.Product;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImp implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final MessageResponse messageResponse;

    @Override
    public OrderDetailResponse createOrderDetail(OrderDetailDTO orderDetailDTO) {
        Order order = existingOrder(orderDetailDTO.getOrderId());
        Product product = existingProduct(orderDetailDTO.getProductId());
        BigDecimal totalPrice = product.getPrice().multiply(BigDecimal.valueOf(orderDetailDTO.getQuantity()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .quantity(orderDetailDTO.getQuantity())
                .price(product.getPrice())
                .totalMoney(totalPrice)
                .color(orderDetailDTO.getColor())
                .build();
        orderDetailRepository.save(orderDetail);
        return mapToOrderDetailResponse(orderDetail);
    }

    @Override
    public List<OrderDetailResponse> getAllDetails(long orderId) {
        existingOrder(orderId);
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        return orderDetails.stream().map(this::mapToOrderDetailResponse).collect(Collectors.toList()); // map danh sách entity sang response
    }

    @Override
    public OrderDetailResponse getById(long id) {
        OrderDetail orderDetail = existingOrderDetail(id);
        return mapToOrderDetailResponse(orderDetail);
    }

    @Override
    public OrderDetailResponse updateOrderDetail(long orderDetailId, OrderDetailDTO orderDetailDTO) {
        OrderDetail existingOrderDetail = existingOrderDetail(orderDetailId);
        Order existingOrder = existingOrder(orderDetailDTO.getOrderId());
        Product existingProduct = existingProduct(orderDetailDTO.getProductId());
        BigDecimal totalMoney = existingProduct.getPrice().multiply(BigDecimal.valueOf(orderDetailDTO.getQuantity()));
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setQuantity(orderDetailDTO.getQuantity());
        existingOrderDetail.setPrice(existingProduct.getPrice());
        existingOrderDetail.setTotalMoney(totalMoney);
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        orderDetailRepository.save(existingOrderDetail);
        return mapToOrderDetailResponse(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(long id) {
        OrderDetail orderDetail = existingOrderDetail(id);
        orderDetailRepository.delete(orderDetail);
        orderDetailRepository.save(orderDetail);
    }

    // map to response
    private OrderDetailResponse mapToOrderDetailResponse(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail,OrderDetailResponse.class);
    }

    // check các thành phần có tồn tại hay không có exception
    private OrderDetail existingOrderDetail(long orderDetailId) {
        return orderDetailRepository.findById(orderDetailId)
                .orElseThrow(()-> new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_ORDER_DETAIL,orderDetailId)));
    }
    private Order existingOrder(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_ORDER,orderId)));
    }
    private Product existingProduct(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(()-> new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_PRODUCT,productId)));
    }
}
