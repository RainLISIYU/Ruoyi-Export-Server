package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description topic模式配置类
 * @date 2024/7/2
 */
public class TopicRabbitConfig {

    public static final String TOPIC_EXCHANGE = "TestTopicExchange";

    public static final String TOPIC_FIRST_QUEUE = "TestTopicQueueFirst";

    public static final String TOPIC_SECOND_QUEUE = "TestTopicQueueSecond";

    public static final String TOPIC_THIRD_QUEUE = "TestTopicQueueThird";

    public static final String TOPIC_TTL_QUEUE = "TestTopicQueueTtl";

    public static final String TOPIC_FIRST_KEY = "test.first";

    public static final String TOPIC_SECOND_KEY = "test.#";

    public static final String TOPIC_THIRD_KEY = "iot_send";

    public static final String TOPIC_TTL_KEY = "ttl.key";


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
        // 设置参数，死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DeadRabbitConfig.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DeadRabbitConfig.DEAD_ROUTING);
        return new Queue(TOPIC_FIRST_QUEUE, true, false, false, arguments);
    }

    /**
     * 设置ttl队列
     *
     * @return 队列
     */
    @Bean
    public Queue topicQueueTtl() {
        // 设置参数，死信队列
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DeadRabbitConfig.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", DeadRabbitConfig.DEAD_ROUTING);
        return new Queue(TOPIC_TTL_QUEUE, true, false, false, arguments);
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

    /**
     * ttl队列绑定
     *
     * @return 绑定
     */
    @Bean
    public Binding bindingExchangeTtl() {
        return BindingBuilder.bind(topicQueueTtl())
                .to(topicExchange()).with(TOPIC_TTL_KEY);
    }

    @Bean("batchQueueRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory batchQueueRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // 批量设置
        factory.setBatchListener(true);
        factory.setConsumerBatchEnabled(true);
        factory.setBatchSize(2);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

}
