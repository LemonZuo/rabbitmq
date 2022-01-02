package com.lemonzuo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LemonZuo
 * @create 2022-01-02 13:20
 */
@Configuration
public class TtlQueueConfig {
    /**
     * 普通交换机
     */
    private static final String NORMAL_EXCHANGE = "X";
    /**
     * 死信交换机
     */
    private static final String DEAD_EXCHANGE = "Y";
    /**
     * 队列QA
     */
    private static final String NORMAL_QUEUE_QA = "QA";
    /**
     * 队列QB
     */
    private static final String NORMAL_QUEUE_QB = "QB";
    /**
     * 队列QC
     */
    private static final String NORMAL_QUEUE_QC = "QC";
    /**
     * 死信队列QD
     */
    private static final String DEAD_QUEUE_QD = "QD";

    @Bean(name = "normalExchange")
    public DirectExchange normalExchange() {
        return new DirectExchange(NORMAL_EXCHANGE);
    }

    /**
     * 构建死信交换机
     * @return
     */
    @Bean(name = "deadExchange")
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE);
    }

    @Bean(name = "normalQueueQa")
    public Queue normalQueueQa() {
        Map<String, Object> arguments = new HashMap<>(16);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 死信队列 routing-key
        arguments.put("x-dead-letter-routing-key", "YD");
        // 消息过期时间 10S
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(NORMAL_QUEUE_QA).withArguments(arguments).build();
    }

    @Bean(name = "normalQueueQb")
    public Queue normalQueueQb() {
        Map<String, Object> arguments = new HashMap<>(16);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 死信队列 routing-key
        arguments.put("x-dead-letter-routing-key", "YD");
        // 消息过期时间 40S
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(NORMAL_QUEUE_QB).withArguments(arguments).build();
    }


    @Bean(name = "normalQueueQc")
    public Queue normalQueueQc() {
        Map<String, Object> arguments = new HashMap<>(16);
        // 设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 死信队列 routing-key
        arguments.put("x-dead-letter-routing-key", "YD");
        return QueueBuilder.durable(NORMAL_QUEUE_QC).withArguments(arguments).build();
    }

    /**
     * 构建死信队列
     * @return
     */
    @Bean(name = "deadQueueQd")
    public Queue deadQueueQd() {
        return QueueBuilder.durable(DEAD_QUEUE_QD).build();
    }

    @Bean
    public Binding queueABingX(Queue normalQueueQa, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueQa).to(normalExchange).with("XA");
    }

    @Bean
    public Binding queueBBingX(Queue normalQueueQb, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueQb).to(normalExchange).with("XB");
    }

    @Bean
    public Binding queueCBingX(Queue normalQueueQc, DirectExchange normalExchange) {
        return BindingBuilder.bind(normalQueueQc).to(normalExchange).with("XC");
    }

    @Bean
    public Binding queueDBingY(Queue deadQueueQd, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueueQd).to(deadExchange).with("YD");
    }

}
