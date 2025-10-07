package com.example.order_service.services;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.UpdateOrderRequest;
import com.example.order_service.enums.Status;
import com.example.order_service.models.Order;
import com.example.order_service.repositories.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    OrderRepository orderRepository;

    @Override
    public Order createOrder(Integer userId, CreateOrderRequest request) {
        Order order = new Order()
                .builder()
                .user_id(userId)
                .orderItems(request.getItems())
                .status(String.valueOf(Status.PENDING))
                .createdAt(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findAllOrder() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrderItem(String OrderId) {orderRepository.deleteById(OrderId);}

    @Override
    public Order updateOrder(String orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new RuntimeException("Order not existed"));
        order.setStatus(request.getStatus());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findOrdersByUserId(int userId) {
        return orderRepository.findByUserId(userId);
    }

}
