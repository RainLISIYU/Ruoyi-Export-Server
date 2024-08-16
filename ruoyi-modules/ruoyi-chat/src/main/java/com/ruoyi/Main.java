package com.ruoyi;

import io.netty.util.concurrent.CompleteFuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/*
 *@Author:cq
 *@Date:2024/7/19 15:33
 */
public class Main {

    private static final int CORE_POOL_SIZE = 5;

    private static final int MAX_POOL_SIZE = 10;

    private static final int KEEP_ALIVE_TIME = 1;

    private static final AtomicInteger atomicInteger = new AtomicInteger(1);

    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), Thread.ofVirtual().name("Virtual.ThreadPool-" + atomicInteger.getAndIncrement()).factory(), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName());
            }
        };
        CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, new Main().threadPoolExecutor);
        System.out.println(Thread.currentThread().getName());
        while (true) {
            if (future.isDone()) {
                System.out.println("已执行完毕");
                break;
            }
        }
    }




}