package com.ruoyi.business.controller;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.mq.configure.TopicRabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lsy
 * @description topic消费者2
 * @date 2024/7/3
 */
@Component
public class TopicReceiverSecond {

    @RabbitListener(queues = TopicRabbitConfig.TOPIC_SECOND_QUEUE)
    public void process(String msg, Message message, Channel channel) throws IOException {
        System.out.println("TopicReceiverSecond消费者收到消息  : " + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
