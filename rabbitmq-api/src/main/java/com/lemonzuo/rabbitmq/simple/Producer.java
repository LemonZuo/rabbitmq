package com.lemonzuo.rabbitmq.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-04-15 22:01
 */
@Slf4j
public class Producer {
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
            // 4.创建交换机，声明队列，绑定关系，路由key,发送消息，接收消息
            // 参数一：队列名称
            // 参数二：队列中消息是否持久化
            // 参数三：队列是否支持多消费者消费、是否进行消息共享
            // 参数四：是否自动删除 最后一个消费者断开连接后是否自动删除
            // 参数五：其他参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 5.准备消息内容
            String message = "RabbitMQ";
            // 6.发送消息给队列
            // 参数一：发送到哪一个交换机
            // 参数二：路由的key值，本次是队列名称
            // 参数三：其他参数信息
            // 参数四：待发送消息的消息体
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("消息发送成功=====>");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 7.关闭通道
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
            // 8.关闭连接
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
