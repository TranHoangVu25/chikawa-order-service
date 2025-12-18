package com.example.order_service.services;

import com.example.order_service.configuration.RabbitMQConfig;
import com.example.order_service.dto.PaymentSuccessEvent;
import com.example.order_service.models.Payment;
import com.example.order_service.repositories.OrderRepository;
import com.example.order_service.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentSuccessConsumer {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @RabbitListener(queues = RabbitMQConfig.ORDER_PAYMENT_QUEUE)
    public void handlePaymentSuccess(PaymentSuccessEvent event) {

        log.info("💰 Payment success received for order {}", event.getOrderId());

        // Idempotency
        if (paymentRepository.existsByPaymentIntentId(
                event.getPaymentIntentId())) {
            log.warn("⚠️ Duplicate payment event ignored");
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

//        orderRepository.markPaid(event.getOrderId());
    }
}

