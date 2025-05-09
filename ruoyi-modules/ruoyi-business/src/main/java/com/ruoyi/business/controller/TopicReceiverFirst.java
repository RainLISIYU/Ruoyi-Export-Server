package com.ruoyi.business.controller;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.mq.configure.TopicRabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author lsy
 * @description topic消费者1
 * @date 2024/7/3
 */
@Component
public class TopicReceiverFirst {

    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
    public void process(String msg, Message message, Channel channel) throws Exception {
        channel.basicQos(1);
        System.out.println("TopicReceiverFirst消费者1接收消息  : " + msg);
//        if (LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() % 2 == 0) {
//            System.out.println("消费者1拒绝消息：" + msg);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            return;
//        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
    public void process1(String msg, Message message, Channel channel) throws Exception {
        channel.basicQos(1);
        System.out.println("TopicReceiverFirst消费者2接收消息：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
