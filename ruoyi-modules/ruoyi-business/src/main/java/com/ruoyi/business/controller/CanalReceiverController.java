package com.ruoyi.business.controller;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.mq.configure.CanalRabbitConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * @author lsy
 * @description Canal接收
 * @date 2025/6/9
 */
@Controller
public class CanalReceiverController {

    /**
     * 消息接收
     *
     * @param message 消息
     * @param channel 通道
     * @throws IOException 异常
     */
    @RabbitListener(queues = CanalRabbitConfig.CANAL_QUEUE)
    public void process(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        System.out.println("canal队列消息：" + msg);
        MessageProperties messageProperties = message.getMessageProperties();
        channel.basicAck(messageProperties.getDeliveryTag(), false);
    }

}
