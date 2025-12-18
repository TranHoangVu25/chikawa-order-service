package com.example.order_service.dto.request;

import com.example.order_service.models.DeliveryAddress;
import com.example.order_service.models.OrderItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    Integer userId;
    String promotionCode;
    List<OrderItem> items;
    DeliveryAddress deliveryAddress;
}
