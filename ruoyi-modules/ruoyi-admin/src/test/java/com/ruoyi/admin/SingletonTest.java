package com.ruoyi.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lsy
 * @description 单例模式 测试类
 * @date 2025/2/7
 */
@SpringBootTest
public class SingletonTest {

    /**
     * Holder Class模式
     */
    private static final class SingletonTestHolder {
        private static final SingletonTest singletonTest = new SingletonTest();
    }

    public static SingletonTest getInstance() {
        return SingletonTestHolder.singletonTest;
    }

    /**
     * 双重锁鉴定
     */
    private static SingletonTest singletonTestLock = null;

    private static SingletonTest getInstanceLock() {
        if (singletonTestLock == null) {
            synchronized (SingletonTest.class) {
                if (singletonTestLock == null) {
                    singletonTestLock = new SingletonTest();
                }
            }
        }
        return singletonTestLock;
    }

    @Test
    public void test() {
        SingletonTest instance = SingletonTest.getInstance();
        System.out.println(instance);
        SingletonTest instance1 = SingletonTest.getInstance();
        System.out.println(instance1);
        SingletonTest instance2 = SingletonTest.getInstanceLock();
        System.out.println(instance2);
        SingletonTest instance3 = SingletonTest.getInstanceLock();
        System.out.println(instance3);
    }

}
