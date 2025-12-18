package com.example.order_service.controllers;

import com.example.order_service.dto.request.UpdateOrderRequest;
import com.example.order_service.grpc.OrderServiceImpl;
import com.example.order_service.models.Order;
import com.example.order_service.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class OrderController {
    OrderService orderService;
    OrderServiceImpl orderServiceImpl;

    @GetMapping("")
    public List<Order> getAllOrders(){
        return orderService.findAllOrder();
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable String orderId){
        orderService.deleteOrderItem(orderId);
    }

    @PutMapping("/{orderId}")
    public Order updateOrder(
            @PathVariable String orderId,
            @RequestBody UpdateOrderRequest request
            ){
        return orderService.updateOrder(orderId,request);
    }

    @GetMapping("/orders")
    public List<Order> getOrdersByUserId(
            @AuthenticationPrincipal Jwt jwt
    ){
        Integer userId = Integer.parseInt(jwt.getClaimAsString("userId"));
        return orderService.findOrdersByUserId(userId);

    }
}
