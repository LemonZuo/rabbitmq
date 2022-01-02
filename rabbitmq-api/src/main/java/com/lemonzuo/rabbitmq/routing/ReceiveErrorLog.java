package com.lemonzuo.rabbitmq.routing;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LemonZuo
 * @create 2021-07-05 21:06
 */
public class ReceiveErrorLog {
    private final static String EXCHANGE_NAME = "DIRECT_LOG";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明直接交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            channel.queueDeclare("disk", false, false, false, null);
            channel.queueBind("disk", EXCHANGE_NAME, "error");
            System.out.println("等待接收消息");
            channel.basicConsume("disk",
                    (consumerTag, message) -> System.out.println(new String(message.getBody(), StandardCharsets.UTF_8)),
                    consumerTag -> System.out.println("取消消费"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
