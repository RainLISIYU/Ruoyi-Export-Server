package com.ruoyi.business.controller;/*
 *@Author:cq
 *@Date:2024/6/26 11:39
 */

import com.rabbitmq.client.Channel;
import com.ruoyi.common.mq.configure.DirectRabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lsy
 * @description direct消费者测试类
 * @date 2024/6/26
 */
@Component
@RabbitListener(queues = DirectRabbitConfig.DIRECT_QUEUE)
public class DirectReceiver {

    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {

        System.out.println("DirectReceiver消费者接收消息：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
