package com.ruoyi.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.utils.uuid.UUID;
import com.ruoyi.common.mq.configure.DirectRabbitConfig;
import com.ruoyi.common.mq.configure.TopicRabbitConfig;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description 消息队列测试Controller
 * @date 2024/6/25
 */
@RestController
@RequestMapping("/direct/producer")
public class RabbitTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * direct模式发送
     *
     * @param msg 消息
     * @return ok
     */
    @GetMapping("/sendDirectMessage")
    public String sendDirectMessage(@RequestParam("msg") String msg) {

        Map<String, Object> map = geneMapParam(msg);
        CorrelationData correlationData = new CorrelationData(String.valueOf(map.get("uuid")));
        // 设置过期时间
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("3000");
        msg = JSON.toJSONString(map);
        Message message = MessageBuilder.withBody(msg.getBytes(StandardCharsets.UTF_8)).andProperties(messageProperties).build();
        rabbitTemplate.convertAndSend(DirectRabbitConfig.DIRECT_EXCHANGE, DirectRabbitConfig.DIRECT_ROUTING, message, correlationData);
        return "ok";
    }

    /**
     * topic模式1
     *
     * @param msg 消息
     * @return ok
     */
    @GetMapping("/sendTopicMessageFirst")
    public String sendTopicMessageFirst(@RequestParam("msg") String msg) {
        // 封装消息
        Map<String, Object> map = geneMapParam(msg);
        msg = JSON.toJSONString(map);
        // 消息配置
        MessageProperties properties = new MessageProperties();
        properties.setExpiration("5000");
        // 设置消息
        Message message = MessageBuilder.withBody(msg.getBytes()).andProperties(properties).setCorrelationId(String.valueOf(map.get("uuid"))).build();
//        CorrelationData correlationData = new CorrelationData(String.valueOf(map.get("uuid")));
        rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, TopicRabbitConfig.TOPIC_FIRST_KEY, message);
        rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, TopicRabbitConfig.TOPIC_TTL_KEY, message);
        return "ok";
    }

    /**
     * topic模式2
     *
     * @param msg 消息
     * @return ok
     */
    @GetMapping("/sendTopicMessageSecond")
    public String sendTopicMessageSecond(@RequestParam("msg") String msg) {
        Map<String, Object> map = geneMapParam(msg);
        CorrelationData correlationData = new CorrelationData(String.valueOf(map.get("uuid")));
        rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, "test.second", JSON.toJSONString(map), correlationData);
        return "ok";
    }

    private Map<String, Object> geneMapParam(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("msg", msg);
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replace("-", "");
        map.put("uuid", uuid);
        return map;
    }

}
