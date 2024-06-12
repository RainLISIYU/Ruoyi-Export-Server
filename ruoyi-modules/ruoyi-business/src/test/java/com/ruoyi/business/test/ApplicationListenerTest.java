package com.ruoyi.business.test;/*
 *@Author:cq
 *@Date:2024/6/5 14:22
 */

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author lsy
 * @description testEvent监听器
 * @date 2024/6/5
 */
@Component
public class ApplicationListenerTest implements ApplicationListener<RunTest.TestEvent> {
    @Override
    public void onApplicationEvent(RunTest.TestEvent event) {
        event.getMessage();
    }
}
