package com.ruoyi.business.controller;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.mq.configure.DeadRabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author lsy
 * @description 死信队列接收测试
 * @date 2025/2/6
 */
@Component
@RabbitListener(queues = DeadRabbitConfig.DEAD_QUEUE)
public class DeadReceiveController {

    @RabbitHandler
    public void process(String msg, Message message, Channel channel) throws IOException {
        System.out.println("死信队列接收消息：" + msg);
        MessageProperties messageProperties = message.getMessageProperties();
        channel.basicAck(messageProperties.getDeliveryTag(), false);
    }

}
