package com.example.order_service.services;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.UpdateOrderRequest;
import com.example.order_service.models.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    Order createOrder(Integer userId, CreateOrderRequest request);

    List<Order> findAllOrder();

    void deleteOrderItem(String orderId);

    Order updateOrder(String orderId, UpdateOrderRequest request);

    List<Order> findOrdersByUserId(int userId);
}
