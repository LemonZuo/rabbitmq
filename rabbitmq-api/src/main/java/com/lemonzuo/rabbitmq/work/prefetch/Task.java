package com.lemonzuo.rabbitmq.work.prefetch;

import cn.hutool.core.util.StrUtil;
import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-07-03 22:54
 * 预取值
 */
public class Task {
    private static final String QUEUE_NAME = "WORK_MODEL_PREFETCH";

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
        Scanner scanner = new Scanner(System.in);
        String str;
        do {
            str = scanner.next();
            // 发送消息，且持久化消息
            // 新增MessageProperties.PERSISTENT_TEXT_PLAIN
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, str.getBytes(StandardCharsets.UTF_8));
        } while (!"end".equalsIgnoreCase(str));
        System.out.println("消息发送完毕");
    }
}
