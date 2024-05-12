package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.CartItemDTO;
import com.project.shopapp.DTOs.OrderDTO;
import com.project.shopapp.DTOs.OrderDetailDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.OrderResponse;
import com.project.shopapp.Repositories.OrderDetailRepository;
import com.project.shopapp.Repositories.OrderRepository;
import com.project.shopapp.Repositories.ProductRepository;
import com.project.shopapp.Repositories.UserRepository;
import com.project.shopapp.Services.OrderService;
import com.project.shopapp.Z_ProcessingProvincialData.ProvincialRepo.ProvinceRepository;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.*;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapper modelMapper; // dùng để convert dữ liệu
    private final MessageResponse messageResponse;
    //private final OrderDetailServiceImp orderDetailServiceImp;

    // hàm tạo tracking number ngẫu nhiên
    private String generateTrackingNumber() {
        int length = 11;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder trackingNumber = new StringBuilder();
        SecureRandom random = new SecureRandom();
        boolean isExist;
        do {
            for (int i = 0; i < length; i++) {
                int randomIndex = random.nextInt(characters.length());
                trackingNumber.append(characters.charAt(randomIndex));
            }
            isExist = isTrackingNumberExist(trackingNumber.toString());
            if (isExist) {
                trackingNumber.setLength(0);
            }
        } while (isExist);
        return trackingNumber.toString();
    }
    //hàm kiểm tra trùng lặp tracking number trong database
    private boolean isTrackingNumberExist(String trackingNumber) {
       Boolean existingOrder = orderRepository.findOrderByTrackingNumber(trackingNumber);
       if (existingOrder != null && existingOrder) {
           return true;
       }
       return false;
    }
    @Override
    public OrderResponse createOrder(OrderDTO orderDTO){
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()-> new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID, orderDTO.getUserId())));
        // Convert từ Dto sang entity -- dùng thư viện model mapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        //cập nhật các trường của đơn hàng từ orderdto
        Order order = new Order();
        modelMapper.map(orderDTO,order);
        order.setUser(user);
        order.setTotalMoney(orderDTO.getTotalMoney());
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // kiểm tra shipping date >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now()))
        {
            throw new DataNotFoundException("Shipping date must be greater than today's date");
        }
        order.setShippingDate(shippingDate);
        order.setTrackingNumber(generateTrackingNumber());
        order.setActive(true);
        orderRepository.save(order);
        // tạo mới chi tiết đơn hàng
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(CartItemDTO cartItemDTO : orderDTO.getCartItemDTOS())
        {
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();
            Product product = productRepository.findById(productId).orElseThrow(()->
                    new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_PRODUCT)));

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setTotalMoney(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
//            orderDetail.setColor("null");
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        modelMapper.typeMap(Order.class,OrderResponse.class);
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public OrderResponse getOrderById(long id) throws Exception {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_ORDER,id)));
        if(!existingOrder.isActive())
            throw new DataNotFoundException(messageResponse.getMessageString(MessageKeys.ORDER_HAS_BEEN_DELETED));
        return mapOrderToOrderResponse(existingOrder);
    }
    @Override
    public List<OrderResponse> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> getOrderByUserId(long userId) throws Exception {
        userRepository.findById(userId)
                .orElseThrow(()->
                        new DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_USER_BY_ID,userId)));
        List<Order> ordersList= orderRepository.findByUserId(userId);
        return ordersList.stream()
                .map(this::mapOrderToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrder(long orderId, OrderDTO orderDTO) throws Exception{
        Order order =  orderRepository.findById(orderId)
                .orElseThrow(()->new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_ORDER,orderId)));
        modelMapper.typeMap(OrderDTO.class,Order.class)
                .addMappings(map->map.skip(Order::setId));
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now().plusDays(1) : orderDTO.getShippingDate();
        modelMapper.map(orderDTO,order);
        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        return mapOrderToOrderResponse(order);
    }
    @Override
    public void setStatusOrder(Long orderId) {

    }

    @Override
    public void deleteOrder(long id) throws Exception{
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new
                        DataNotFoundException(messageResponse.getMessageString(MessageKeys.NOT_FOUND_ORDER,id)));
        order.setActive(false);
        orderRepository.save(order);
    }

    // sánh xạ từ entity sang response
    private OrderResponse mapOrderToOrderResponse(Order order) {
        return modelMapper.map(order, OrderResponse.class);
    }
//    private Order mapDtoToOrderEntity(OrderDTO orderDTO)
//    {
//        return modelMapper.map(orderDTO,Order.class);
//    }
}
