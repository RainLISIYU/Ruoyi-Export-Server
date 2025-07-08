package com.ruoyi.common.core.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    private static final int MAXIMUM_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME = 1000;

    private final AtomicInteger count = new AtomicInteger(1);

    private final ThreadFactory threadFactory = Thread.ofVirtual()
            .name("Virtual Thread - " + count.getAndIncrement() + " ==> ")
            .factory();

//    @Bean
//    public ThreadPoolExecutor threadPoolExecutor() {
//        try(ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
//                CORE_POOL_SIZE,
//                MAXIMUM_POOL_SIZE,
//                KEEP_ALIVE_TIME,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingQueue<>(),
//                threadFactory,
//                new ThreadPoolExecutor.CallerRunsPolicy())) {
//            return threadPoolExecutor;
//        }
//    }

    @Primary
    @Bean
    public ThreadPoolTaskExecutor traceIdThreadPool() {
        ThreadPoolTaskExecutor threadPool = new TraceIdThreadPool();
        threadPool.setCorePoolSize(CORE_POOL_SIZE);//核心线程数
        threadPool.setMaxPoolSize(MAXIMUM_POOL_SIZE);//最大线程数
        threadPool.setKeepAliveSeconds(60);//线程存活时间
        threadPool.setQueueCapacity(10000);
        threadPool.setAllowCoreThreadTimeOut(false);
        threadPool.setThreadFactory(threadFactory);
        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//拒绝策略， 调用者执行
        return threadPool;
    }

}
