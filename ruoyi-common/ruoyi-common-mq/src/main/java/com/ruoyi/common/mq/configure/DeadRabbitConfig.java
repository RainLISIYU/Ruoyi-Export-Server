package com.ruoyi.common.mq.configure;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

/**
 * @author lsy
 * @description 死信队列配置
 * @date 2025/2/6
 */
public class DeadRabbitConfig {

    public static final String DEAD_EXCHANGE = "TestDeadExchange";

    public static final String DEAD_QUEUE = "TestDeadQueue";

    public static final String DEAD_ROUTING = "TestDeadRouting";

    @Bean
    public Queue deadQueue() {
        return new Queue(DEAD_QUEUE, true);
    }

    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(DEAD_EXCHANGE, true, false, null);
    }

    @Bean
    public Binding bindingDead() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(DEAD_ROUTING);
    }


}
