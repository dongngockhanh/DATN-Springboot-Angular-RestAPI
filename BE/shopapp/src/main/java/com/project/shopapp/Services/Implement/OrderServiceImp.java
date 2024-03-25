package com.project.shopapp.Services.Implement;

import com.project.shopapp.DTOs.OrderDTO;
import com.project.shopapp.DTOs.responses.MessageResponse;
import com.project.shopapp.DTOs.responses.OrderResponse;
import com.project.shopapp.Repositories.OrderRepository;
import com.project.shopapp.Repositories.UserRepository;
import com.project.shopapp.Services.OrderService;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.models.Order;
import com.project.shopapp.models.OrderStatus;
import com.project.shopapp.models.User;
import com.project.shopapp.untils.MessageKeys;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper; // dùng để convert dữ liệu
    private final MessageResponse messageResponse;

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
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);
        // kiểm tra shipping date >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now()))
        {
            throw new DataNotFoundException("Shipping date must be greater than today's date");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
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
    public List<OrderResponse> getAllOrders(long userId) throws Exception {
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
