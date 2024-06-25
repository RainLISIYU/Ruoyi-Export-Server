package com.ruoyi.business.test;/*
 *@Author:cq
 *@Date:2024/6/5 14:00
 */

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

/**
 * @author lsy
 * @description 测试类
 * @date 2024/6/5
 */
@SpringBootTest
public class RunTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test() {
        TestEvent testEvent = new TestEvent("");
        testEvent.setMessage("Test Event!!");
        applicationContext.publishEvent(testEvent);
    }

    class TestEvent extends ApplicationEvent {

        private String message;

        public TestEvent(Object source) {
            super(source);
        }

        public void getMessage() {
            System.out.println(message);
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    interface DutyLink {

        void handler();
    }

    static class DutyOne implements DutyLink {

        private DutyLink next;

        public DutyOne(DutyLink next) {
            this.next = next;
        }

        @Override
        public void handler() {
            System.out.println("任务1");
            if (this.next != null) {
                next.handler();
            }
        }
    }

    static class DutyTwo implements DutyLink {

        private DutyLink next;

        public DutyTwo (DutyLink next) {
            this.next = next;
        }

        @Override
        public void handler() {
            System.out.println("任务2");
            if (next != null) {
                next.handler();
            }
        }
    }

    public static void main(String[] args) {
        DutyLink dutyTwo = new RunTest.DutyTwo(null);
        DutyLink dutyOne = new RunTest.DutyOne(dutyTwo);
        dutyOne.handler();
    }

}
