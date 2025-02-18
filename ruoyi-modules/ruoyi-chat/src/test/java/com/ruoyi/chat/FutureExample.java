package com.ruoyi.chat;

import com.lsy.baselib.crypto.algorithm.DESede;
import com.lsy.baselib.crypto.exception.DESedeException;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

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
                        Thread.sleep(4000);
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

    @Test
    public void syncCondition() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        // Thread1 Run
        threadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "等待");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "继续执行");
            }catch (InterruptedException e) {
                System.err.println(e.getMessage());
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + "结束执行");
        });
        // Thread2 Run
        threadPoolExecutor.execute(() -> {
            try {
                // 保证Thread1先执行
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            System.out.println(Thread.currentThread().getName() + "开始执行");
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "唤醒");
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "唤醒后继续");
            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + "结束执行");
        });
        Thread.sleep(10000);
    }

    @Test
    public void proxyTest() {
        ProxyImage proxyImage = new ProxyImage(new RealImage("test.jpg"));
        Image image = (Image) proxyImage.getInstance();
        image.display();
    }

    @Test
    public void enhancerTest() {
        ImageEnhancer imageEnhancer = new ImageEnhancer(new RealImage("file1.jpg"));
        Image image = (Image) imageEnhancer.getInstance();
        image.display();
    }

    /**
     * 命令模式
     */
    @Test
    public void commandPattern() {
        Broker broker = new Broker();
        Stock stock = new Stock("产品1", 10);
        broker.takeOrder(new BuyStock(stock));
        broker.takeOrder(new SellStock(stock));
        broker.placeOrders();
    }

    @Test
    public void IteratorPattern() {
        NameRepository nameRepository = new NameRepository(new String[]{"Robert", "John", "Julie", "Lora"});
        Iterator<String> iterator = nameRepository.getIterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        System.out.println(gcd(180, 32));
    }

    // 求最大公约数 弗洛伊德算法
    int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

}

interface Iterator<T> {
    boolean hasNext();
    T next();
}

interface Container<T> {
    Iterator<T> getIterator();
}

class NameRepository implements Container<String> {

    private String[] names;

    public NameRepository(String[] names) {
        this.names = names;
    }

    @Override
    public Iterator<String> getIterator() {
        return new Iterator<>() {

            int index = 0;

            @Override
            public boolean hasNext() {
                return index < names.length;
            }

            @Override
            public String next() {
                return names[index++];
            }
        };
    }

}

class Stock {
    private String name;
    private int quantity;

    public Stock(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public void buy() {
        System.out.println("Stock [ Name: " + name + ", Quantity: " + quantity + " ] bought");
    }

    public void sell() {
        System.out.println("Stock [ Name: " + name + ", Quantity: " + quantity + " ] sold");
    }
}

interface Order {
    void execute();
}

class BuyStock implements Order {
    private Stock stock;
    public BuyStock(Stock stock) {
        this.stock = stock;
    }
    @Override
    public void execute() {
        stock.buy();
    }
}

class SellStock implements Order {
    private Stock stock;
    public SellStock(Stock stock) {
        this.stock = stock;
    }
    @Override
    public void execute() {
        stock.sell();
    }
}

class Broker {
    private List<Order> orderList = new ArrayList<>();
    public void takeOrder(Order order) {
        orderList.add(order);
    }

    public void placeOrders() {
        for (Order order : orderList) {
            order.execute();
        }
        orderList.clear();
    }
}

interface Image {
    void display();
}

class RealImage implements Image {

    private final String fileName;

    public RealImage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void display() {
        System.out.println("Displaying " + fileName);
    }

    private void loadFromDisk(String fileName) {
        System.out.println("Loading " + fileName);
    }

}

class ProxyImage implements InvocationHandler {

    private Object image;

    public ProxyImage (Object target) {
        this.image = target;
    }

    public Object getInstance() {
        return Proxy.newProxyInstance(image.getClass().getClassLoader(), image.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(image, args);
    }
}

class EnhancerImage implements Image {

    @Override
    public void display() {
        System.out.println("Enhancer Display .");
    }
}

class ImageEnhancer implements MethodInterceptor {

    private Object target;

    public ImageEnhancer(Object target) {
        this.target = target;
    }

    public Object getInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create(new Class[] {String.class}, new Object[] {"file2.jpg"});
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return proxy.invokeSuper(obj, args);
    }

}