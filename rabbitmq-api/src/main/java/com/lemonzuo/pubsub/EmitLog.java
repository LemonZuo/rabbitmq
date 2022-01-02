package com.lemonzuo.pubsub;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author LemonZuo
 * @create 2021-07-04 23:23
 */
public class EmitLog {
    private final static String EXCHANGE_NAME = "logs";
    public static void main(String[] args) {
        try {
            // 获取信道
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                // 推送消息
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println("消息推送成功：".concat(message));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
