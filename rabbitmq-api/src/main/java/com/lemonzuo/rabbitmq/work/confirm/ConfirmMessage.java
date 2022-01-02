package com.lemonzuo.rabbitmq.work.confirm;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.util.IdUtil;
import com.lemonzuo.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author LemonZuo
 * @create 2021-07-04 15:24
 */
public class ConfirmMessage {
    public static void main(String[] args) {
        // 单个确认 11312MS
        // confirmSingle();
        // 批量发布确认 200MS
        // confirmBatch();
        // 异步确认 120MS
        // confirmAsync();
    }

    public static void confirmSingle() {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 开启发布确认
            channel.confirmSelect();
            // 60e164ef22158c3ee7788a69
            String queueName = IdUtil.objectId();
            // 申明队列
            channel.queueDeclare(queueName, true, false, false, null);
            TimeInterval timer = DateUtil.timer();
            for (int i = 0; i < 1000; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
                boolean confirms = channel.waitForConfirms();
                if (confirms) {
                    System.out.println("发布消息成功");
                }
            }
            System.out.println("耗时：" + timer.interval());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void confirmBatch() {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 开启发布确认
            channel.confirmSelect();
            // 60e167db22159c5f90a586ac
            String queueName = IdUtil.objectId();
            // 申明队列
            channel.queueDeclare(queueName, true, false, false, null);
            // 批量大小
            int batchSize = 100;
            TimeInterval timer = DateUtil.timer();
            for (int i = 0; i < 1000; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
                if ((i + 1) % batchSize == 0) {
                    boolean confirms = channel.waitForConfirms();
                    if (confirms) {
                        System.out.println("发布消息成功");
                    }
                }
            }
            System.out.println("耗时：" + timer.interval());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void confirmAsync() {
        try {
            Channel channel = RabbitMqUtils.getChannel();
            assert channel != null;
            // 开启发布确认
            channel.confirmSelect();
            // 线程安全有序的哈希表
            ConcurrentSkipListMap<Long, String> container = new ConcurrentSkipListMap<>();
            // 60e16b132215319e0d197747
            String queueName = IdUtil.objectId();
            // 申明队列
            channel.queueDeclare(queueName, true, false, false, null);
            // 消息监听器
            // 成功回调
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
                System.out.println("确认的消息编号：" + deliveryTag);
                if (multiple) {
                    ConcurrentNavigableMap<Long, String> confirmed = container.headMap(deliveryTag);
                    confirmed.clear();
                } else {
                    container.remove(deliveryTag);
                }
            };
            // 失败回调
            ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
                System.out.println("未确认的消息编号：" + deliveryTag);
            };
            channel.addConfirmListener(ackCallback, nackCallback);
            TimeInterval timer = DateUtil.timer();
            for (int i = 0; i < 1000; i++) {
                String message = String.valueOf(i);
                channel.basicPublish("", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN,
                        message.getBytes(StandardCharsets.UTF_8));
                container.put(channel.getNextPublishSeqNo(), message);
            }
            System.out.println("耗时：" + timer.interval());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
