package com.lemonzuo.rabbitmq.dead;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LemonZuo
 * @create 2021-07-06 12:46
 * 死信队列正常消费者
 */
public class DeadConsumer {
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String DEAD_QUEUE = "dead_queue";
    private static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            // 死信交换机与死信队列绑定
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);
            System.out.println("死信消费者等待接收消息");
            channel.basicConsume(DEAD_QUEUE, true, (consumerTag, message) -> {
                System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            }, consumerTag -> {
                System.out.println("取消消费");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
