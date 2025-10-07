package com.example.order_service.controllers;

import com.example.order_service.dto.request.CreateOrderRequest;
import com.example.order_service.dto.request.UpdateOrderRequest;
import com.example.order_service.models.Order;
import com.example.order_service.models.OrderItem;
import com.example.order_service.services.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    OrderService orderService;

    @PostMapping("")
    public Order createOrder(@AuthenticationPrincipal Jwt jwt,
                             @RequestBody CreateOrderRequest request
                                      ) {
        Integer userId = Integer.valueOf(jwt.getClaimAsString("sub"));

        return orderService.createOrder(userId, request);
    }

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
}
