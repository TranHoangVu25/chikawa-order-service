package com.example.order_service.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Document(collection = "order_service")
public class Order {
    @Id
    private String id;

    @NotNull
    @Indexed
    @Field("user_id")
    private Integer userId;

    private String status = "pending";

    @Field("promotion_code")
    private String promotionCode;

    @Field("confirmed_at")
    private LocalDateTime confirmedAt;

    @Field("canceled_at")
    private LocalDateTime canceledAt;

    @Field("payment_id")
    private String paymentId;

    @Field("paid_at")
    private LocalDateTime paidAt;

    @Field("order_items")
    private List<OrderItem> orderItems = new ArrayList<>();

    private Payment payment;

    @Field("delivery_address")
    private DeliveryAddress deliveryAddress;


    public void pending() {
        this.status = "pending";
    }

    public void paid() {
        this.status = "paid";
        this.paidAt = LocalDateTime.now();
    }

    public void canceled() {
        this.status = "canceled";
        this.canceledAt = LocalDateTime.now();
    }

    public void confirmed() {
        this.status = "confirmed";
        this.confirmedAt = LocalDateTime.now();
    }

    public boolean isPending() {
        return "pending".equals(status);
    }

    public boolean isPaid() {
        return "paid".equals(status);
    }

    public boolean isCanceled() {
        return "canceled".equals(status);
    }

    public boolean isConfirmed() {
        return "confirmed".equals(status);
    }

    public double totalPrice() {
        return orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
