package com.lemonzuo.rabbitmq.simple;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-04-15 22:01
 */
@Slf4j
public class Consumer {
    private static final String QUEUE_NAME = "SIMPLE_MODEL";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            // 1.创建连接工厂
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("8.134.12.132");
            factory.setPort(5672);
            factory.setUsername("admin");
            factory.setPassword("admin");
            factory.setVirtualHost("/");
            // 2.创建连接Connection
            connection = factory.newConnection("producer");
            // 3.创建通道Channel
            channel = connection.createChannel();
            // 4. 接收消息
            // 消费成功
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println("收到的消息为========>");
                System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            };
            // 取消消费
            CancelCallback cancelCallback = consumerTag -> {
                System.out.println("取消消费消息");
            };
            // 参数一：消费的队列名称
            // 参数二：消费成功后是否自动应答
            // 参数三：消费者消费成功回调
            // 参数四：消费者取消消费回调
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 5.关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            // 6.关闭连接
            if (connection != null && connection.isOpen()) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
