package com.example.order_service.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payments")
public class Payment {
    @Id
    private String id;
    private String orderId;
    private Integer userId;
    @Indexed(unique = true)
    private String paymentIntentId;

    private String checkoutSessionId;
    private Long amount;
    private String currency;
    private String status; // PAID | FAILED | REFUNDED
    private String paidAt;
}
