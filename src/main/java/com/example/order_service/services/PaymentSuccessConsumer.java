package com.example.order_service.services;

import com.example.order_service.configuration.RabbitMQConfig;
import com.example.order_service.dto.PaymentSuccessEvent;
import com.example.order_service.models.Order;
import com.example.order_service.models.Payment;
import com.example.order_service.repositories.OrderRepository;
import com.example.order_service.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessConsumer {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    @Transactional
    public void handlePaymentSuccess(PaymentSuccessEvent event) {

        log.info("Payment success received for order {}", event.getOrderId());

        // Idempotency
        if (paymentRepository.existsByPaymentIntentId(
                event.getPaymentIntentId())) {
            log.warn("Duplicate payment event ignored");
            return;
        }

        Payment payment = Payment.builder()
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .paymentIntentId(event.getPaymentIntentId())
                .checkoutSessionId(event.getCheckoutSessionId())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .paidAt(event.getPaidAt())
                .status("PAID")
                .build();

        paymentRepository.save(payment);

        String orderId = event.getOrderId();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("In payment receive service. Order not found"));

        LocalDateTime ldt =
                Instant.parse(event.getPaidAt())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

        order.setStatus("PAID");
        order.setPaymentId(payment.getId());
        order.setPaidAt(ldt);
        order.setPayment(payment);
        orderRepository.save(order);
    }
}

