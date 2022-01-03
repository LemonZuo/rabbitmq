package com.lemonzuo.controller;

import cn.hutool.core.util.IdUtil;
import com.lemonzuo.config.ConfirmConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LemonZuo
 * @create 2022-01-02 23:44
 */
@Slf4j
@RestController
@Api(tags = "消息发布确认")
@RequestMapping("/confirm")
public class ConfirmController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("发送发布确认消息")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "msg", value = "消息内容", paramType = "path")
    })
    @GetMapping("/sendConfirmMessage/{msg}")
    public void sendConfirmMessage(@PathVariable String msg) {
        rabbitTemplate.setConfirmCallback((CorrelationData correlationData, boolean ack, String cause) -> {
            if (ack) {
                log.info("交换机接收消息成功: ID:{}", (correlationData != null ? correlationData.getId() : ""));
            } else {
                log.info("交换机未收到Id:{}的消息，原因:{}", (correlationData != null ? correlationData.getId() : ""), cause);
            }
        });
        CorrelationData correlationData = new CorrelationData();
        rabbitTemplate.convertAndSend(ConfirmConfig.EXCHANGE_NAME, ConfirmConfig.ROUTING_KEY, msg, correlationData);
        log.info("exchangeName:{}, 发送消息内容为:{}", ConfirmConfig.EXCHANGE_NAME, msg);

        correlationData = new CorrelationData();
        String exchangeName = ConfirmConfig.EXCHANGE_NAME.concat(IdUtil.randomUUID());
        rabbitTemplate.convertAndSend(exchangeName, ConfirmConfig.ROUTING_KEY, msg, correlationData);
        log.info("exchangeName:{}, 发送消息内容为:{}", exchangeName, msg);
    }
}
