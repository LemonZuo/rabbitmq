package com.lemonzuo.rabbitmq.work;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-07-03 22:54
 * 队列持久化
 */
public class TaskQueueDurable {
    private static final String QUEUE_NAME = "WORK_MODEL_ACK_DURABLE";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMqUtils.getChannel();
        // 参数一：队列名称
        // 参数二：队列中消息是否持久化
        // 参数三：队列是否支持多消费者消费、是否进行消息共享
        // 参数四：是否自动删除 最后一个消费者断开连接后是否自动删除
        // 参数五：其他参数
        // 队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        for (int i = 11; i <= 20; i++) {
            channel.basicPublish("", QUEUE_NAME, null, String.valueOf(i).getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("消息发送完毕");
    }
}
