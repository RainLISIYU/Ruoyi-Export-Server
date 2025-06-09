package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description Canal消息队列配置
 * @date 2025/6/9
 */
public class CanalRabbitConfig {

    public static final String CANAL_EXCHANGE = "CanalExchange";

    public static final String CANAL_QUEUE = "CanalQueue";

    public static final String CANAL_TEST_ROUTING = "CanalTestRouting";

    @Bean
    public Queue canalQueue() {
        // 设置参数，死信队列
        Map<String, Object> arguments = new HashMap<>();
//        arguments.put("x-dead-letter-exchange", DeadRabbitConfig.DEAD_EXCHANGE);
//        arguments.put("x-dead-letter-routing-key", DeadRabbitConfig.DEAD_ROUTING);
        return new Queue(CANAL_QUEUE, true, false, false, arguments);
    }

    @Bean
    public FanoutExchange  canalExchange() {
        return new FanoutExchange(CANAL_EXCHANGE);
    }

    @Bean
    public Binding bindingCanal() {
        return BindingBuilder
                .bind(canalQueue())
                .to(canalExchange());
    }

}
