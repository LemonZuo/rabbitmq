package com.lemonzuo.rabbitmq.routing;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author LemonZuo
 * @create 2021-07-05 21:17
 */
public class DirectLog {
    private final static String EXCHANGE_NAME = "DIRECT_LOG";

    public static void main(String[] args) {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String message = scanner.next();
                String[] str = message.split(";");

                // 推送消息
                channel.basicPublish(EXCHANGE_NAME, str[0], null, str[1].getBytes(StandardCharsets.UTF_8));
                System.out.println("消息推送成功：消息类型".concat(str[0]).concat(" 消息内容：").concat(str[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
