package com.ruoyi.business.test;

import com.ruoyi.Main;
import com.ruoyi.business.config.BaiduAipUtils;
import com.ruoyi.business.service.RedisCodeService;
import com.ruoyi.system.api.RemoteBaiduAipService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lsy
 * @description 测试
 * @date 2025/1/7
 */
@SpringBootTest
@Slf4j
public class BaiduAiTest {

    @Resource
    private RemoteBaiduAipService remoteBaiduAipService;

    @Resource
    private RedisCodeService redisCodeService;

    private InheritableThreadLocal<String> threadLocal1 = new InheritableThreadLocal<>();

    private InheritableThreadLocal<String> threadLocal2 = new InheritableThreadLocal<>();

    private final AtomicInteger count = new AtomicInteger(1);
    ThreadFactory threadFactory = Thread.ofVirtual().name("Virtual Thread - " + count.getAndIncrement() + " ==> ").factory();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
        10,
        10,
        500,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        threadFactory,
        new ThreadPoolExecutor.CallerRunsPolicy());


    @Resource
    private BaiduAipUtils baiduAipUtils;

    @Test
    public void test() {
        String method = "GET";
        String path = "/v1/BCE-BEARER/token";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "iam.bj.baidubce.com");
        headers.put("Content-Type", "application/json");
        String authorizationString = baiduAipUtils.generateSignKey(method, path, headers);
        headers.put("Authorization", authorizationString);
        String token = remoteBaiduAipService.getToken(headers, 2000L);
        log.info("获取token：{}", token);
    }

    @Test
    public void futureTest() throws InterruptedException {
        threadLocal1.set("threadLocal1");
        threadLocal2.set("threadLocal2");
        CompletableFuture.runAsync(() -> {
            log.info("==子线程执行==,[{},{}]", threadLocal1.get(), threadLocal2.get());
        }, threadPoolExecutor);
        Thread.sleep(1000);
        log.info("==主线程执行==，[{},{}]", threadLocal1.get(), threadLocal2.get());
    }

}
