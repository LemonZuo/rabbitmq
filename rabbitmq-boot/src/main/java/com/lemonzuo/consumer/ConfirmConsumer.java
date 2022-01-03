package com.lemonzuo.consumer;

import com.lemonzuo.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author LemonZuo
 * @create 2022-01-02 23:48
 */
@Slf4j
@Component
public class ConfirmConsumer {
    @RabbitListener(queues = ConfirmConfig.QUEUE_NAME)
    public void confirmReceiveMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到的消息内容为:{}", msg);
    }
}
