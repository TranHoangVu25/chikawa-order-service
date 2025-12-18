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

    // ==========================
    // Order & User
    // ==========================
    private String orderId;
    private Integer userId;

    // ==========================
    // Stripe info
    // ==========================
    @Indexed(unique = true)
    private String paymentIntentId;

    private String checkoutSessionId;

    // ==========================
    // Money
    // Stripe dùng minor unit (VND)
    // ==========================
    private Long amount;
    private String currency;

    // ==========================
    // Status
    // ==========================
    private String status; // PAID | FAILED | REFUNDED

    // ==========================
    // Time
    // ==========================
    private String paidAt;
}
