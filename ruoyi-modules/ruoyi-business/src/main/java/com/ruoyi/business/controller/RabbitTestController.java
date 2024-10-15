package com.ruoyi.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.utils.uuid.UUID;
import com.ruoyi.common.mq.configure.DirectRabbitConfig;
import com.ruoyi.common.mq.configure.TopicRabbitConfig;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public String sendDirectMessage(@RequestParam String msg) {

        Map<String, Object> map = geneMapParam(msg);
        CorrelationData correlationData = new CorrelationData(String.valueOf(map.get("uuid")));
        rabbitTemplate.convertAndSend(DirectRabbitConfig.DIRECT_EXCHANGE, DirectRabbitConfig.DIRECT_ROUTING, JSON.toJSONString(map), correlationData);
        return "ok";
    }

    /**
     * topic模式1
     *
     * @param msg 消息
     * @return ok
     */
    @GetMapping("/sendTopicMessageFirst")
    public String sendTopicMessageFirst(@RequestParam String msg) {
        Map<String, Object> map = geneMapParam(msg);
        CorrelationData correlationData = new CorrelationData(String.valueOf(map.get("uuid")));
        rabbitTemplate.convertAndSend(TopicRabbitConfig.TOPIC_EXCHANGE, TopicRabbitConfig.TOPIC_FIRST_KEY, JSON.toJSONString(map), correlationData);
        return "ok";
    }

    /**
     * topic模式2
     *
     * @param msg 消息
     * @return ok
     */
    @GetMapping("/sendTopicMessageSecond")
    public String sendTopicMessageSecond(@RequestParam String msg) {
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
