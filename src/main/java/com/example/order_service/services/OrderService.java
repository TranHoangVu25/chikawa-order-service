package com.example.order_service.services;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.UpdateOrderRequest;
import com.example.order_service.models.Order;
import com.example.order_service.models.OrderItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order createOrder(Integer userId, CreateOrderRequest request);

    List<Order> findAllOrder();

    void deleteOrderItem(String orderId);

    Order findByUserId(Integer userId);

    Order updateOrder(String orderId, UpdateOrderRequest request);
}
