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
public class NormalConsumerMaxLength {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String DEAD_EXCHANGE = "dead_exchange";
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String DEAD_QUEUE = "dead_queue";
    private static final String NORMAL_ROUTING_KEY = "normal";
    private static final String DEAD_ROUTING_KEY = "dead";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明死信交换机
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明死信队列
            channel.queueDeclare(DEAD_QUEUE, false, false, false, null);
            channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, DEAD_ROUTING_KEY);


            // 声明普通交换机
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            // 声明普通队列
            Map<String, Object> arguments = new HashMap<>(4);
            // 绑定死信交换机
            arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
            // 死信队列使用的routing-key
            arguments.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY);
            // 队列最大长度
            arguments.put("x-max-length", 6);

            channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

            // 普通交换机与普通队列绑定
            channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, NORMAL_ROUTING_KEY);
            System.out.println("普通消费者等待接收消息");
            channel.basicConsume(NORMAL_QUEUE, true, (consumerTag, message) -> {
                System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            }, consumerTag -> {
                System.out.println("取消消费");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
