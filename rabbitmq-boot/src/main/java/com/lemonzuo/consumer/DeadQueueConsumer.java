package com.lemonzuo.consumer;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LemonZuo
 * @create 2022-01-02 14:01
 */
@Slf4j
@Component
public class DeadQueueConsumer {

    @RabbitListener(queues = "QD")
    public void receiveMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("当前时间:{}, 收到死信队列的消息:{}", DateUtil.now(),msg);
    }
}
