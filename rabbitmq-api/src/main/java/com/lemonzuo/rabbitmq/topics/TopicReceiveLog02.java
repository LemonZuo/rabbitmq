package com.lemonzuo.rabbitmq.topics;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LemonZuo
 * @create 2021-07-05 21:41
 */
public class TopicReceiveLog02 {
    private static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) {
        try {
            // 获取信道
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明主题交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 声明队列
            channel.queueDeclare("Q2", false, false, false, null);
            // 交换机绑定队列
            channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
            channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");
            channel.basicConsume("Q2", true, (consumerTag, message) -> {
                System.out.println("routeKey: ".concat(message.getEnvelope().getRoutingKey()).concat(" message:").concat(new String(message.getBody(), StandardCharsets.UTF_8)));
            },consumerTag -> System.out.println("取消消费"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
