package com.example.order_service.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Document(collection = "order_service")
public class Order {
    @Id
    String orderId;
    Integer userId;
    List<OrderItem> orderItems;
    String status;
    LocalDateTime createdAt;
    Double totalPrice;
}
