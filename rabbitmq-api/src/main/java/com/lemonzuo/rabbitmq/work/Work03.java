package com.lemonzuo.rabbitmq.work;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author LemonZuo
 * @create 2021-07-03 22:45
 * 手动应答
 */
public class Work03 {
    private static final String QUEUE_NAME = "WORK_MODEL_ACK";

    public static void main(String[] args) throws IOException, TimeoutException {
        System.out.println("WORK-3-start");
        Channel channel = RabbitMqUtils.getChannel();
        // 消费成功
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("收到的消息为========>");
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
            // 消息的标记
            long deliveryTag = message.getEnvelope().getDeliveryTag();
            // 是否批量应答本信道内所有消息
            boolean multiple = false;
            // 执行应答
            channel.basicAck(deliveryTag, multiple);
        };
        // 取消消费
        CancelCallback cancelCallback = consumerTag -> {
            System.out.println("取消消费消息");
        };
        // 异步执行消费,手动应答
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, cancelCallback);
    }
}
