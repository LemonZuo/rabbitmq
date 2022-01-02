package com.lemonzuo.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-07-03 22:36
 */
public class RabbitMqUtils {
    public static Channel getChannel() {
        // 1.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("8.134.12.132");
        factory.setPort(5672);
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setVirtualHost("/");
        // 2.创建连接Connection
        Connection connection = null;
        try {
            connection = factory.newConnection("producer");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
        // 3.创建通道Channel
        try {
            return connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
