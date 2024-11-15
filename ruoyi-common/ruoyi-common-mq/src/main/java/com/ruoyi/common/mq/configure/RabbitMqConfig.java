package com.ruoyi.common.mq.configure;/*
 *@Author:cq
 *@Date:2024/6/26 14:12
 */

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lsy
 * @description rabbitmq基础配置类
 * @date 2024/6/26
 */
public class RabbitMqConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitmqConfirmCallback rabbitmqConfirmCallback;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(rabbitmqConfirmCallback);
        rabbitTemplate.setReturnsCallback(rabbitmqConfirmCallback);
    }

//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }

//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(messageConverter());
//        return rabbitTemplate;
//    }

//    @Bean
//    public SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(messageConverter());
//        return factory;
//    }

}
