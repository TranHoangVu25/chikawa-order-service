package com.example.order_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // ===== Payment -> Order =====
    public static final String PAYMENT_EXCHANGE = "payment_exchange";
    public static final String PAYMENT_ORDER_QUEUE = "payment_order_queue";

    // ===== Order -> Cart =====
    public static final String ORDER_EXCHANGE = "order_exchange";
    public static final String ORDER_CART_QUEUE = "order_cart_queue";

    /* ================= PAYMENT -> ORDER ================= */

    @Bean
    public FanoutExchange paymentFanoutExchange() {
        return new FanoutExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue paymentOrderQueue() {
        return QueueBuilder.durable(PAYMENT_ORDER_QUEUE).build();
    }

    @Bean
    public Binding bindPaymentOrderQueue(
            Queue paymentOrderQueue,
            FanoutExchange paymentFanoutExchange
    ) {
        return BindingBuilder
                .bind(paymentOrderQueue)
                .to(paymentFanoutExchange);
    }

    /* ================= ORDER -> CART ================= */

    @Bean
    public FanoutExchange orderFanoutExchange() {
        return new FanoutExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Queue orderCartQueue() {
        return QueueBuilder.durable(ORDER_CART_QUEUE).build();
    }

    @Bean
    public Binding bindOrderCartQueue(
            Queue orderCartQueue,
            FanoutExchange orderFanoutExchange
    ) {
        return BindingBuilder
                .bind(orderCartQueue)
                .to(orderFanoutExchange);
    }

    /* ================= COMMON ================= */

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jackson2JsonMessageConverter());
        return template;
    }
}

