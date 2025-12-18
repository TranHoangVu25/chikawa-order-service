package com.example.order_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentSuccessEvent {

    // ==========================
    // Order & User
    // ==========================
    private String orderId;
    private Integer userId;

    // ==========================
    // Stripe identifiers
    // ==========================
    private String paymentIntentId;
    private String checkoutSessionId;

    // ==========================
    // Money (minor unit)
    // ==========================
    private Long amount;
    private String currency;

    // ==========================
    // Metadata
    // ==========================
    private String paidAt; // ISO-8601
}
