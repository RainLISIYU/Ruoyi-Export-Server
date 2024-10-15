package com.ruoyi.common.mq.configure;

import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author lsy
 * @description rabbitmq自定义回调方法
 * @date 2024/6/26
 */
@Component
public class RabbitmqConfirmCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            System.out.println("消息发送成功");
        }else {
            jdbcTemplate.execute("INSERT INTO rabbit_log (uuid) values ('" + correlationData.getId() + "')");
            System.out.println("消息发送失败:" + correlationData.getId());
        }
    }

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        Message message = returned.getMessage();
        message.getMessageProperties().getHeader("");
        String body = new String(returned.getMessage().getBody());
        jdbcTemplate.execute("INSERT INTO rabbit_log (body) values ('" + body + "')");
        System.out.println("消息没有路由到队列");
        System.out.println("replyCOde: " + returned.getReplyCode());
        System.out.println("replyText: " + returned.getReplyText());
        System.out.println("exchange: " + returned.getExchange());
        System.out.println("message body: " + body);
    }
}
