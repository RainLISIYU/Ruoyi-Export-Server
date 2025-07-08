package com.ruoyi.business.controller;

import com.rabbitmq.client.Channel;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.mq.configure.TopicRabbitConfig;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lsy
 * @description topic消费者1
 * @date 2024/7/3
 */
@Component
@Slf4j
public class TopicReceiverFirst {

    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
    public void process(String msg, Message message, Channel channel) throws Exception {
        String traceId = (String) Optional.ofNullable(message.getMessageProperties().getHeader(SecurityConstants.TRACE_ID)).orElse("");
        MDC.put(SecurityConstants.TRACE_ID, traceId);
        log.info("1开始时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        channel.basicQos(1);
        System.out.println("TopicReceiverFirst消费者1接收消息  : " + msg);
//        if (LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() % 2 == 0) {
//            System.out.println("消费者1拒绝消息：" + msg);
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//            return;
//        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("1结束时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
    public void process1(String msg, Message message, Channel channel) throws Exception {
        String traceId = (String) Optional.ofNullable(message.getMessageProperties().getHeader(SecurityConstants.TRACE_ID)).orElse("");
        MDC.put(SecurityConstants.TRACE_ID, traceId);
        log.info("2开始时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        channel.basicQos(1);
        System.out.println("TopicReceiverFirst消费者2接收消息：" + msg);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("2结束时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

//    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
//    public void process3(String msg, Message message, Channel channel) throws Exception {
//        log.info("3开始时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        channel.basicQos(1);
//        System.out.println("TopicReceiverFirst消费者3接收消息：" + msg);
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.info("3结束时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//    }
//
//    @RabbitListener(queues = TopicRabbitConfig.TOPIC_FIRST_QUEUE)
//    public void process4(String msg, Message message, Channel channel) throws Exception {
//        log.info("4开始时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        channel.basicQos(1);
//        System.out.println("TopicReceiverFirst消费者4接收消息：" + msg);
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        log.info("4结束时间：{}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//    }

    public static void main(String[] args) {
        Semaphore semaphore1 = new Semaphore(1);
        Semaphore semaphore2 = new Semaphore(0);
        Semaphore semaphore3 = new Semaphore(0);
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    semaphore1.acquire();
                    if (i % 2 == 0) {
                        System.out.print("A");
                        semaphore2.release();
                    } else {
                        System.out.print("A");
                        semaphore3.release();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore2.acquire();
                    System.out.print("B");
                    semaphore1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore3.acquire();
                    System.out.print("C");
                    semaphore1.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }

    public static void printNum() {
        int n = 5;
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger flag = new AtomicInteger(1);
        ReentrantLock lock = new ReentrantLock();
        Condition c1 = lock.newCondition();
        Condition c2 = lock.newCondition();
        Condition c3 = lock.newCondition();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock();
                try {
                    if (flag.get() != 1) {
                        c1.await();
                    }
                    System.out.println("Thread 1 : " + count.incrementAndGet());
                    flag.set(2);
                    c2.signal();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock();
                try {
                    if (flag.get() != 2) {
                        c2.await();
                    }
                    System.out.println("Thread 2 : " + count.incrementAndGet());
                    flag.set(3);
                    c3.signal();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < n; i++) {
                lock.lock();
                try {
                    if (flag.get() != 3) {
                        c3.await();
                    }
                    System.out.println("Thread 3 : " + count.incrementAndGet());
                    flag.set(1);
                    c1.signal();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
        });
        t3.start();
        t2.start();
        t1.start();
    }

    public static void printOddAndEven() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            int n = sc.nextInt();
            ReentrantLock lock = new ReentrantLock();
            Condition conditionOdd = lock.newCondition();
            Condition conditionEven = lock.newCondition();
            Condition conditionZero = lock.newCondition();
            AtomicInteger count = new AtomicInteger(0);
            Thread threadZero = new Thread(() -> {
                for (int i = 0; i < n - 1; i++) {
                    try {
                        lock.lock();
                        count.incrementAndGet();
                        System.out.print(0);
                        if (i % 2 == 0) {
                            conditionOdd.signal();
                        } else {
                            conditionEven.signal();
                        }
                        conditionZero.await();
                    } catch (Exception e) {
                        System.err.print(e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }
            });
            Thread threadOdd = new Thread(() -> {
                for (int i = 1; i < n; i += 2) {
                    try {
                        lock.lock();
                        if (count.get() != 1) {
                            conditionOdd.await();
                        } else {
                            count.getAndIncrement();
                        }
                        System.out.print(i);
                        conditionZero.signal();
                    } catch (Exception e) {
                        System.err.print(e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }
            });
            Thread threadEven = new Thread(() -> {
                for (int i = 2; i < n + 1; i += 2) {
                    try {
                        lock.lock();
                        conditionEven.await();
                        System.out.print(i);
                        conditionZero.signal();
                    } catch (Exception e) {
                        System.err.print(e.getMessage());
                    } finally {
                        lock.unlock();
                    }
                }
            });
            threadZero.start();
            threadOdd.start();
            threadEven.start();
        }
    }

}
