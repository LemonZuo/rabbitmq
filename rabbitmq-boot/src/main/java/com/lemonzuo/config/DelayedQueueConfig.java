package com.lemonzuo.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于延迟插件延迟队列配置列
 * @author LemonZuo
 * @create 2022-01-02 20:28
 */
@Configuration
public class DelayedQueueConfig {
    /**
     * 延迟队列交换机名称
     */
    public static final String DELAYED_PLUGIN_EXCHANGE_NAME = "delayed_plugin_exchange";
    /**
     * 延迟队列名称
     */
    public static final String DELAYED_PLUGIN_QUEUE_NAME = "delayed_plugin_queue";
    /**
     * 延迟队列 routing Key
     */
    public static final String DELAYED_PLUGIN_ROUTING_KEY = "delayed_plugin_routing_key";

    /**
     * 声明基于插件的交换机
     * @return
     */
    @Bean(name = "delayedPluginExchange")
    public CustomExchange delayedPluginExchange() {
        Map<String, Object> arguments = new HashMap<>(16);
        // 延迟类型
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_PLUGIN_EXCHANGE_NAME, "x-delayed-message",true, false, arguments);
    }


    /**
     * 生命队列
     * @return
     */
    @Bean(name = "delayedPluginQueue")
    public Queue delayedPluginQueue() {
        return QueueBuilder.durable(DELAYED_PLUGIN_QUEUE_NAME).build();
    }

    @Bean(name = "pluginQueueBindingExchange")
    public Binding pluginQueueBindingExchange(Exchange delayedPluginExchange, Queue delayedPluginQueue) {
        return BindingBuilder.bind(delayedPluginQueue).to(delayedPluginExchange).with(DELAYED_PLUGIN_ROUTING_KEY).noargs();
    }
}
