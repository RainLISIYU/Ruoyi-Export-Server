package com.ruoyi;

import io.netty.util.concurrent.CompleteFuture;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Scanner;
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

        Flux<String> name = Flux.just("Tom", "jerry", "sofia", "joker");
        Flux<Integer> upName = name.map(Integer::valueOf);
        upName.subscribe(
                System.out::println,
                System.out::println,
                () -> System.out.println("mission complete"));

        Mono<String> mono = Mono.just("Hello World");
        mono.subscribe(System.out::println);

        Scanner sc = new Scanner(System.in);
        int l = sc.nextInt();
        if (l % 2 == 0) {
            return;
        }
        for (int i = 0; i < l; i++) {
            if (i == 0 || i == l / 2 || i == l-1) {
                for (int j = 0; j < l; j++) {
                    System.out.print("*");
                }
                System.out.println();
                continue;
            }
            for (int j = 0; j < l; j++) {
                if (i < l/2) {
                    if (j == 0 || j == l/2 || j == l-1 || j == i || j == l-i-1) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                } else {
                    if (j == 0 || j == l/2 || j == l-1 || j == l - i - 1 || j == i) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }




}