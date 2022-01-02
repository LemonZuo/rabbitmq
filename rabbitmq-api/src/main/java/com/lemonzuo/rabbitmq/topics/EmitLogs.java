package com.lemonzuo.rabbitmq.topics;

import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * @author LemonZuo
 * @create 2021-07-05 22:25
 */
public class EmitLogs {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) {
        Map<String, String> messages = new HashMap<>();
        messages.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
        messages.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        messages.put("quick.orange.fox", "被队列 Q1 接收到");
        messages.put("azy.brown.fox", "被队列 Q2 接收到");
        messages.put("lazy.pink.rabbit", "虽然满足两个绑定但只被队列 Q2 接收一次");
        messages.put("quick.brown.fox", "不匹配任何绑定不会被任何队列接收到会被丢弃");
        messages.put("quick.orange.male.rabbit", "是四个单词不匹配任何绑定会被丢弃");
        messages.put("lazy.orange.male.rabbit", "是四个单词但匹配 Q2");
        try {
            // 获取信道
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 声明主题交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            messages.forEach((key, value) -> {
                try {
                    // 推送消息
                    channel.basicPublish(EXCHANGE_NAME, key, null, value.getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
