package com.example.order_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_EXCHANGE = "payment.fanout.exchange";
    public static final String ORDER_PAYMENT_QUEUE = "order.payment.queue";

    @Bean
    public FanoutExchange paymentFanoutExchange() {
        return new FanoutExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public Queue orderPaymentQueue() {
        return QueueBuilder
                .durable(ORDER_PAYMENT_QUEUE)
                .build();
    }

    @Bean
    public Binding bindOrderQueue(
            Queue orderPaymentQueue,
            FanoutExchange paymentFanoutExchange
    ) {
        return BindingBuilder
                .bind(orderPaymentQueue)
                .to(paymentFanoutExchange);
    }
}
