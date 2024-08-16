package com.ruoyi.chat;

import com.lsy.baselib.crypto.algorithm.DESede;
import com.lsy.baselib.crypto.exception.DESedeException;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lsy
 * @description CompletableFuture测试
 * @date 2024/8/5
 */
public class FutureExample {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME = 1;

    private static final AtomicInteger atomicInteger = new AtomicInteger(1);

    public ThreadPoolExecutor virtualThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new PriorityBlockingQueue<>(), Thread.ofVirtual().uncaughtExceptionHandler((t, e) -> System.err.println("异常捕获" + e.getMessage())).name("Virtual.ThreadPool-" +atomicInteger.getAndIncrement()).factory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("ThreadPool-" + atomicInteger.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, e) -> System.err.println("异常捕获" + e.getMessage()));
            return thread;
        }
    }, new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void runAsyncExample() {
        Runnable runnable = () -> {
            try {
                Thread.sleep(1000);
            }catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            System.out.println(Thread.currentThread().getName());
        };
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(runnable, threadPoolExecutor);
        System.out.println(Thread.currentThread().getName());
        while (true) {
            if (completableFuture.isDone()) {
                System.out.println("任务执行完成");
                break;
            }
        }
    }

    @Test
    public void thenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                    System.out.println("SupplyAsync " + Thread.currentThread().getName());
                    int i = 1/0;
                    return "result";
                }, threadPoolExecutor)
                .exceptionally(e -> {
                    System.err.println("Run Error!");
                    return e.getMessage();
                })
                .thenAccept(result -> {
                    System.out.println("result is " + result);
                    System.out.println("thenAccept " + Thread.currentThread().getName());
                });
        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            System.out.println("Execute " + Thread.currentThread().getName());
            int i = 1 / 0;
        });
        System.out.println(Thread.currentThread().getName());
        while (true) {
            if (completableFuture.isDone()) {
                System.out.println("任务执行完成");
                break;
            }
        }
    }

    public Unsafe getUnSafe() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            return (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String getSecretKey () throws DESedeException {
        byte[] key = DESede.createKey(DESede.DESEDE_KEY_112_BIT);
        return Base64.getEncoder().encodeToString(key);
    }

    @Test
    public void DesTest() throws DESedeException {
        String secretKey = getSecretKey();
        System.out.println(secretKey);
        Base64.Decoder decoder = Base64.getDecoder();
        String data = "{\n" +
                "\"excode\":\"2002\",\n" +
                "\"data\":{\n" +
                "\"jnlo\":\"20211028142820039\",\n" +
                "\"channelId\":\"th0006\",\n" +
                "\"inTime\":\"2021-10-2814:28:20\",\n" +
                "\"userCode\":\"018018497\",\n" +
                "\"oldMeterCode\":\"031090682910\",\n" +
                "\"meterCode\":\"031090682916\",\n" +
                "\"userType\":\"1\",\n" +
                "\"payType\":\"3\",\n" +
                "\"meterType\":\"1\",\n" +
                "\"dcCode\":\"\",\n" +
                "\"reading\":\"0\",\n" +
                "\"cycleUsedGas\":\"0\",\n" +
                "\"presaving\":\"100.00\"\n" +
                "}\n" +
                "}";
        byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] secCode = decoder.decode(secretKey);
        byte[] encrypt = DESede.encrypt(data.getBytes(StandardCharsets.UTF_8), secCode, iv);
        String sessionData = new String(Base64.getEncoder().encode(encrypt));
        System.out.println(sessionData);
        byte[] deData = decoder.decode(sessionData);
        byte[] decrypt = DESede.decrypt(deData, secCode, iv);
        System.out.println(new String(decrypt, StandardCharsets.UTF_8));
    }

}
