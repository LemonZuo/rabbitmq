package com.lemonzuo.rabbitmq.dead;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.nio.charset.StandardCharsets;

/**
 * @author LemonZuo
 * @create 2021-07-06 16:14
 * 死信生产者
 */
public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String NORMAL_ROUTING_KEY = "normal";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 构建超时参数 单位毫秒
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                    .expiration("10000").build();
            for (int i = 1; i < 11; i++) {
                String message = "info" + i;
                channel.basicPublish(NORMAL_EXCHANGE, NORMAL_ROUTING_KEY, null, message.getBytes(StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
