package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsy
 * @description rabbitmq直连模式配置类
 * @date 2024/6/25
 */
public class DirectRabbitConfig {

    public static final String DIRECT_QUEUE = "TestDirectQueue";

    public static final String DIRECT_EXCHANGE = "TestDirectExchange";

    public static final String DIRECT_ROUTING = "TestDirectRouting";

    /**
     * 消息队列初始化
     *
     * @return 队列
     */
    @Bean
    public Queue TestDirectQueue() {
        return new Queue(DIRECT_QUEUE, true);
    }

    /**
     * Direct交换机初始化
     *
     * @return 交换机
     */
    @Bean
    public DirectExchange TestDirectExchange() {
        return new DirectExchange(DIRECT_EXCHANGE);
    }

    /**
     * 将队列和交换机绑定，设置匹配键
     *
     * @return 绑定实体
     */
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue())
                .to(TestDirectExchange()).with(DIRECT_ROUTING);
    }

}
