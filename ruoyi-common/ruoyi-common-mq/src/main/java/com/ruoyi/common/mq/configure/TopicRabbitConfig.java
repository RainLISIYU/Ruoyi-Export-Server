package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsy
 * @description topic模式配置类
 * @date 2024/7/2
 */
@Configuration
public class TopicRabbitConfig {

    public static final String TOPIC_EXCHANGE = "TestTopicExchange";

    public static final String TOPIC_FIRST_QUEUE = "TestTopicQueueFirst";

    public static final String TOPIC_SECOND_QUEUE = "TestTopicQueueSecond";

    public static final String TOPIC_THIRD_QUEUE = "TestTopicQueueThird";

    public static final String TOPIC_FIRST_KEY = "test.first";

    public static final String TOPIC_SECOND_KEY = "test.#";

    public static final String TOPIC_THIRD_KEY = "iot_send";



    /**
     * 初始化topic交换机
     *
     * @return topic交换机
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }

    /**
     * 初始化队列1
     *
     * @return 队列1
     */
    @Bean
    public Queue topicQueueFirst() {
        return new Queue(TOPIC_FIRST_QUEUE);
    }

    /**
     * 队列3
     *
     * @return 队列3
     */
    @Bean
    public Queue topicQueueThird() {
        return new Queue(TOPIC_THIRD_QUEUE);
    }

    /**
     * 初始化队列2
     *
     * @return 队列2
     */
    @Bean
    public Queue topicQueueSecond() {
        return new Queue(TOPIC_SECOND_QUEUE);
    }

    /**
     * 绑定queue1
     *
     * @return 绑定实体
     */
    @Bean
    public Binding bindingExchangeFirst() {
        return BindingBuilder.bind(topicQueueFirst())
                .to(topicExchange()).with(TOPIC_FIRST_KEY);
    }

    /**
     * 绑定queue2
     *
     * @return 绑定实体
     */
    @Bean
    public Binding bindingExchangeSecond() {
        return BindingBuilder.bind(topicQueueSecond())
                .to(topicExchange()).with(TOPIC_SECOND_KEY);
    }

    /**
     * 绑定queue3
     *
     * @return 绑定队列3
     */
    @Bean
    public Binding bindingExchangeThird() {
        return BindingBuilder.bind(topicQueueThird())
                .to(topicExchange()).with(TOPIC_THIRD_KEY);
    }


}
