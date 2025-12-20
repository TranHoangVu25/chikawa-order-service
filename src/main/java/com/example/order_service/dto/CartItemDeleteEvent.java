package com.example.order_service.dto;

import com.example.order_service.enums.Action;
import com.example.order_service.models.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDeleteEvent {
    List<OrderItem> orderItems;
    Action action;
    Integer userId;
}
