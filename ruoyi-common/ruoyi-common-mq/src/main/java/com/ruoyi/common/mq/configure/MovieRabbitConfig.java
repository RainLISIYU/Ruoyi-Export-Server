package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description 电影信息处理MQ
 * @date 2025/4/16
 */
public class MovieRabbitConfig {

    // 队列标识
    public static final String MOVIE_QUEUE = "movieQueue";

    // 交换机标识
    public static final String MOVIE_EXCHANGE = "movieExchange";

    // 路由键标识
    public static final String MOVIE_ROUTING = "movieRouting";

    @Bean
    public Queue movieQueue() {
        // 设置参数，死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DeadRabbitConfig.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DeadRabbitConfig.DEAD_ROUTING);
        return new Queue(MOVIE_QUEUE, true, false, false, arguments);
    }

    @Bean
    public TopicExchange movieExchange() {
        return new TopicExchange(MOVIE_EXCHANGE);
    }

    @Bean
    public Binding bindingMovie() {
        return BindingBuilder
                .bind(movieQueue())
                .to(movieExchange())
                .with(MOVIE_ROUTING);
    }

}
