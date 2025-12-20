package com.example.order_service.services;

import com.example.order_service.configuration.RabbitMQConfig;
import com.example.order_service.dto.CartItemDeleteEvent;
import com.example.order_service.dto.PaymentSuccessEvent;
import com.example.order_service.enums.Action;
import com.example.order_service.models.Order;
import com.example.order_service.models.Payment;
import com.example.order_service.repositories.OrderRepository;
import com.example.order_service.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_ORDER_QUEUE)
    @Transactional // chạy từ đầu đến cuối, nếu lỗi chạy lại từ đầu
    public void handlePaymentSuccess(PaymentSuccessEvent event) {

        log.info("Payment success received for order {}", event.getOrderId());

        // Kiểm tra payment đã tồn tại chưa
        if (paymentRepository.existsByPaymentIntentId(
                event.getPaymentIntentId())) {
            log.warn("Duplicate payment event ignored");
            return;
        }

        //tạo payment
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

        //cập nhật thông tin order
        LocalDateTime ldt =
                Instant.parse(event.getPaidAt())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

        order.setStatus("PAID");
        order.setPaymentId(payment.getId());
        order.setPaidAt(ldt);
        order.setPayment(payment);

        Action action = Action.DELETE_CART_ITEMS;

        //dùng rabbitmq để truyền message xóa qua cart_service
        CartItemDeleteEvent e = new CartItemDeleteEvent().builder()
                .orderItems(order.getOrderItems())
                .action(action)
                .userId(order.getUserId())
                .build();

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                "",
                e
        );
        orderRepository.save(order);
    }
}

