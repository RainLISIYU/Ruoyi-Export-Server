package com.ruoyi.business.controller;

import com.ruoyi.business.service.RedisCodeService;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.context.SecurityContextHolder;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.utils.SecurityUtils;
import com.ruoyi.system.api.RemoteBaiduAipService;
import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.model.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lsy
 * @description 调用Dubbo服务
 * @date 2025/2/17
 */
@RestController
@RequestMapping("/dubbo")
@Slf4j
public class DubboController {

    @DubboReference(loadbalance = "roundrobin", mock = "com.ruoyi.system.api.factory.RemoteDubboServiceMock", retries = 3)
    private RemoteDubboService remoteDubboService;

    @Resource
    private RemoteUserService remoteUserService;

    @Resource
    private RemoteBaiduAipService remoteBaiduAipService;

    @Autowired
    private RedisCodeService redisCodeService;

    private final AtomicInteger countA = new AtomicInteger(1);
    ThreadFactory threadFactory = Thread.ofVirtual().name("Virtual Thread - " + countA.getAndIncrement() + " ==> ").factory();
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            500,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Resource
    private Redisson redisson;

    private Integer count = 5;

    private Integer countAsync = 5;

    /**
     * Dubbo远程调用
     *
     * @return 结果
     */
    @GetMapping("/getInfo")
    public String getInfo() throws InterruptedException {
        System.out.println("Dubbo请求线程：" + Thread.currentThread() + ",当前用户：" + SecurityUtils.getUsername());
        RLock admin = redisson.getLock("admin");
        Map<String, Object> headers = new HashMap<>();
        headers.put("content-type", "application/json");
        headers.put("connect-timeout", 1000);
        Map<String, Object> params = new HashMap<>();
        params.put("name", "test");
        params.put("count", 1);
        System.out.println(remoteBaiduAipService.getSearch(headers, params));
        System.out.println("非锁库存为：" + (countAsync == 0 ? 0 : --countAsync));
        String result = "Empty";
        try {
            if (admin.tryLock(10, TimeUnit.SECONDS)) {
                String info = remoteDubboService.getInfo();
                if (count == 0) {
                    System.out.println("库存为0");
                } else {
                    System.out.println("购买成功，库存为" + --count);
                }
                Thread.sleep(1000);
                System.out.println("Dubbo服务admin解锁");
                result = info;
            }
        } finally {
            admin.unlock();
        }
        CompletableFuture<Void> cf1 = CompletableFuture.runAsync(() -> redisCodeService.redisSetOperation(), threadPoolExecutor);
        CompletableFuture<Void> cf2 = CompletableFuture.runAsync(() -> redisCodeService.redisZsetOperation(), threadPoolExecutor);
        CompletableFuture.allOf(cf1, cf2)
                .thenRunAsync(() -> redisCodeService.redisStrOperation(), threadPoolExecutor)
                .thenRunAsync(() -> redisCodeService.redisHashOperation(), threadPoolExecutor)
                .thenRunAsync(() -> redisCodeService.redisListOperation(), threadPoolExecutor)
                .thenRunAsync(() -> redisCodeService.redissonBloomFilter(), threadPoolExecutor)
                .exceptionally(e -> {
                    log.error(e.getMessage());
                    return null;
                });
        return result;
    }

    /**
     * Feign远程调用
     *
     * @return 结果
     */
    @GetMapping("/getUser/{id}")
    public String getUser(@PathVariable String id) {
        System.out.println("当前登录用户：" + SecurityUtils.getUsername() + " 请求线程：" + Thread.currentThread());
        RLock admin = redisson.getLock("admin");
        System.out.println(Thread.currentThread().getName());
        try {
            if (admin.tryLock(10, TimeUnit.SECONDS)) {
                System.out.println("feign服务开始");
                R<LoginUser> userInfo = remoteUserService.getUserInfo("admin", SecurityConstants.INNER);
                LoginUser data = userInfo.getData();
                log.info(String.valueOf(data));
                String result = "Empty";
                if (! Objects.isNull(data)) {
                    result = data.getSysUser().getUserName();
                }
                System.out.println("feign服务释放");
                return result;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            admin.unlock();
        }

        return "Empty";
    }

}
