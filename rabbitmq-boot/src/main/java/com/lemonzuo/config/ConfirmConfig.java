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
    public static final String BACK_EXCHANGE_NAME = "back.exchange";
    public static final String QUEUE_NAME = "confirm.queue";
    public static final String ROUTING_KEY = "confirm.routing.key";
    public static final String BACKUP_QUEUE_NAME = "back.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean
    public Exchange confirmExchange() {
        // 不使用交换机备份时使用
        // return new DirectExchange(EXCHANGE_NAME, true, false);
        // 使用交换机备份时使用
        // ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).withArgument("alternate-exchange", BACK_EXCHANGE_NAME).build();
        return ExchangeBuilder.directExchange(EXCHANGE_NAME).durable(true).alternate(BACK_EXCHANGE_NAME).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(QUEUE_NAME).build();
    }

    @Bean
    public Binding confirmBinding(Queue confirmQueue, Exchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(ROUTING_KEY).noargs();
    }

    @Bean
    public Exchange backupExchange() {
        return new FanoutExchange(BACK_EXCHANGE_NAME, true, false);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupBinding(Queue backupQueue, Exchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange).with("").noargs();
    }

    @Bean
    public Binding warningBinding(Queue warningQueue, Exchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange).with("").noargs();
    }
}
