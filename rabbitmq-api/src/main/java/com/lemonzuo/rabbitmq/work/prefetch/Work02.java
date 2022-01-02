package com.lemonzuo.rabbitmq.work.prefetch;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author LemonZuo
 * @create 2021-07-04 11:34
 */
public class Work02 {
    private static final String QUEUE_NAME = "WORK_MODEL_PREFETCH";

    public static void main(String[] args) {
        Channel channel = RabbitMqUtils.getChannel();

        try {
            TimeUnit.SECONDS.sleep(10);
            assert channel != null;
            // prefetchCount: 0-公平/轮询分发，1-不公平分发，>=2 -预取值
            int prefetchCount = 5;
            channel.basicQos(prefetchCount);
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
                long deliveryTag = message.getEnvelope().getDeliveryTag();
                channel.basicAck(deliveryTag, false);
            };
            channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
                System.out.println("取消消费");
            });
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
