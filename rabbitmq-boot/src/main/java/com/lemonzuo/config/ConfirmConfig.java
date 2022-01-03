package com.lemonzuo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认配置
 * @author LemonZuo
 * @create 2022-01-02 23:31
 */
@Configuration
public class ConfirmConfig {
    public static final String EXCHANGE_NAME = "confirm.exchange";
    public static final String QUEUE_NAME = "confirm.queue";
    public static final String ROUTING_KEY = "confirm.routing.key";

    @Bean
    public Exchange confirmExchange() {
        return new DirectExchange(EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmBinding(Queue confirmQueue, Exchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY).noargs();
    }
}
