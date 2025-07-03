package com.ruoyi.common.core.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lsy
 * @description 线程池配置类
 * @date 2024/6/21
 */
@Configuration
public class ThreadPoolConfig {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAXIMUM_POOL_SIZE = 100;

    private static final int KEEP_ALIVE_TIME = 1000;

    private final AtomicInteger count = new AtomicInteger(1);

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = Thread.ofVirtual().name("Virtual Thread - " + count.getAndIncrement() + " ==> ").factory();
        try(ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy())) {
            return threadPoolExecutor;
        }
    }

}
