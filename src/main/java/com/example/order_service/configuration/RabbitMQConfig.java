package com.example.order_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "payment_exchange";
    public static final String ORDER_QUEUE = "payment_order_queue";

    @Bean
    public FanoutExchange paymentFanoutExchange() {
        return new FanoutExchange(EXCHANGE);
    }

    @Bean
    public Queue orderPaymentQueue() {
        return QueueBuilder
                .durable(ORDER_QUEUE)
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

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
