package com.lemonzuo.controller;

import cn.hutool.core.date.DateUtil;
import com.lemonzuo.config.DelayedQueueConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author LemonZuo
 * @create 2022-01-02 13:54
 */
@Api(tags = "发送消息")
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @ApiOperation("发送消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息内容", paramType = "path")
    })
    @GetMapping("/sendMsg/{msg}")
    public void sendMsg(@PathVariable String msg) {
        log.info("当前时间:{}, 发送一条消息给两个TTL队列:{}", DateUtil.now(), msg);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL1为10s的消息队列"+msg);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL1为40s的消息队列"+msg);
    }

    @ApiOperation("发送消息带过期时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息内容", paramType = "path"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间（毫秒）", paramType = "path")
    })
    @GetMapping("/sendMessageWithExpireTime/{msg}/{expireTime}")
    public void sendMessageWithExpireTime(@PathVariable String msg, @PathVariable String expireTime) {
        log.info("当前时间:{}, 发送一条时长:{}毫秒的消息给QC队列:{}", DateUtil.now(), expireTime, msg);

        rabbitTemplate.convertAndSend("X", "XC", msg, messagePostProcessor ->{
            // 设置过期时间
            messagePostProcessor.getMessageProperties().setExpiration(expireTime);
            return messagePostProcessor;
        });
    }

    @ApiOperation("基于插件发送消息带过期时间")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "msg", value = "消息内容", paramType = "path"),
            @ApiImplicitParam(name = "expireTime", value = "过期时间（毫秒）", paramType = "path")
    })
    @GetMapping("/sendMessageWithExpireTimeWithPlugin/{msg}/{expireTime}")
    public void sendMessageWithExpireTimeWithPlugin(@PathVariable String msg, @PathVariable Integer expireTime) {
        log.info("当前时间:{}, 发送一条时长:{}毫秒的消息给delayed_plugin_queue队列:{}", DateUtil.now(), expireTime, msg);

        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_PLUGIN_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_PLUGIN_ROUTING_KEY, msg, messagePostProcessor ->{
            // 设置过期时间
            messagePostProcessor.getMessageProperties().setDelay(expireTime);
            return messagePostProcessor;
        });

    }
}
