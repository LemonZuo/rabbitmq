package com.lemonzuo.rabbitmq.work;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-07-03 22:45
 */
public class Work01 {
    private static final String QUEUE_NAME = "WORK_MODEL";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 消费成功
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("收到的消息为========>");
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
        };
        // 取消消费
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("取消消费消息");
        };
        System.out.println("WORK-2-start");
        // 异步执行消费
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        System.out.println("WORK-2-end");
    }
}
