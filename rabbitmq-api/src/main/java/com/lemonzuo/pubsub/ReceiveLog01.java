package com.lemonzuo.pubsub;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author LemonZuo
 * @create 2021-07-04 23:13
 */
public class ReceiveLog01 {
    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        try {
            // 获取信道
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            // 生成临时队列
            String queueName = channel.queueDeclare().getQueue();
            // 交换机与队列进行绑定
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            // 消费成功回调
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            };
            // 取消消费回调
            CancelCallback cancelCallback = consumerTag -> {
                System.out.println("取消消费");
            };
            // 接收消息
            System.out.println("ReceiveLog-01 等待接收消息");
            channel.basicConsume(queueName, deliverCallback, cancelCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
