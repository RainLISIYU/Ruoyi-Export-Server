package com.ruoyi.business.service.impl;

import com.ruoyi.business.service.RedisCodeService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author lsy
 * @description redis基本操作
 * @date 2025/3/13
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Service
@Slf4j
public class RedisCodeServiceImpl implements RedisCodeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private Redisson redisson;

    @Override
    public void redisStrOperation() {
        ValueOperations<String, Object> strOperations =
                redisTemplate.opsForValue();
        // 赋值并指定过期时间
        strOperations.set("str-test", "test", 10, TimeUnit.SECONDS);
        Boolean test = strOperations.setIfAbsent("str-test", "test", 10, TimeUnit.SECONDS);
        log.info("SETNX命令结果：{}", test);
        // 查询value
        Object o = strOperations.get("str-test");
        log.info("str-test存储的值为：{}", o);
        // 赋值若不存在
        Boolean ff = strOperations.setIfAbsent("str-test", "ff", 10, TimeUnit.SECONDS);
        Boolean gg = strOperations.setIfAbsent("str-test1", "gg", 10, TimeUnit.SECONDS);
        log.info("setNx1:{}, setNx2:{}", ff, gg);
        // 追加
        Integer ss = strOperations.append("str-test", "ss");
        log.info("append:{}", ss);
        // lua脚本
        Object execute = redisTemplate.execute(
                RedisScript.of("if (redis.call('exists', KEYS[1]) == 1) then" +
                        " redis.call('expire', KEYS[1], ARGV[1]); " +
                        " end;" +
                        " return redis.call('ttl', KEYS[1]);"),
                List.of("str-test"),
                15);
        log.info("lua脚本执行结果：{}", execute);
        // 过期时间查询
        Long expire = redisTemplate.getExpire("str-test");
        Long expire1 = redisTemplate.getExpire("str-test1");
        log.info("str-test过期时间：{}, str-test1过期时间：{}", expire, expire1);
        // 位图操作
        strOperations.setBit("bitmap", 2, true);
        strOperations.setBit("bitmap", 3, true);
        log.info("bitmap查询:{},{}", strOperations.getBit("bitmap", 2), strOperations.getBit("bitmap", 1));
        redisTemplate.expire("bit-map", 10, TimeUnit.SECONDS);
    }

    @Override
    public void redisHashOperation() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("test-map", "a1", "count");
        Boolean b = hashOperations.putIfAbsent("test-map", "a1", "cc");
        Object v1 = hashOperations.get("test-map", "a1");
        Object v2 = hashOperations.get("test-map", "a2");
        log.info("Hash Hsetnx结果：{}，存在value值：{}，不存在value值：{}", b, v1, v2);
        Map map = new HashMap();
        map.put("b2", "jjiui");
        map.put("c3", "jijsodfj");
        map.put("d4", "jisdifu");
        map.put("e5", "jfisdhfisdf");
        hashOperations.putAll("test-map", map);
        Map entries = hashOperations.entries("test-map");
        StringBuilder str = new StringBuilder();
        entries.forEach((key, value) -> str.append("[").append(key).append(":").append(value).append("]"));
        log.info("Hash中的键值对：{}", str);
        try {
            Long b2 = hashOperations.increment("test-map", "b2", 1L);
            log.info("Hash自增结果：{}", b2);
        } catch (Exception e) {
            log.error("Hash异常：{}", e.getMessage());
        }
        // 删除key
        Long a1 = hashOperations.delete("test-map", "a1", "j2");
        log.info("Hash删除结果：{}", a1);
        // 删除后键值读取
        entries = hashOperations.entries("test-map");
        StringBuilder str1 = new StringBuilder();
        entries.forEach((key, value) -> str1.append("[").append(key).append(":").append(value).append("]"));
        log.info("Hash中的键值对：{}", str1);
        // 删除键
        log.info("删除键：{}", redisTemplate.delete("test-map"));
    }

    @Override
    public void redisListOperation() {
        // 创建队列[d,c,b,a]
        ListOperations listOperations = redisTemplate.opsForList();
        listOperations.leftPush("test-list", "a");
        listOperations.leftPushAll("test-list", "b", "c", "d");
        Object index = listOperations.index("test-list", 3L);
        log.info("test-list index：{}", index);
        // 右出1
        Object o = listOperations.rightPop("test-list");
        log.info("队列右pop:{}", o);
        // 左出全部
        List list = listOperations.leftPop("test-list", 5L);
        StringBuilder str1 = new StringBuilder();
        assert list != null;
        list.forEach(str1::append);
        log.info("队列左出5：{}", str1);
        // 重新设置队列
        listOperations.leftPush("test-list", "a");
        listOperations.leftPushAll("test-list", "b", "c", "d", "e", "d", "e", "d");
        // 当前队列
        log.info("当前队列：{}", listOperations.range("test-list", 0, 10));
        listOperations.set("test-list", 0, "iii");
        // lrem操作
        Long d = listOperations.remove("test-list", 1, "d");
        // 输出列表
        list = listOperations.range("test-list", 0, 10);
        log.info("替换后队列：{}", list);
        Long d1 = listOperations.remove("test-list", -1, "d");
        log.info("rem操作后返回值：{}, {}", d, d1);
        // 输出列表
        list = listOperations.range("test-list", 0, 10);
        log.info("替换后队列：{}", list);
        listOperations.trim("test-list", 0, 4);
        // 输出列表
        list = listOperations.range("test-list", 0, 10);
        log.info("替换后队列：{}", list);
        // 阻塞pop
        CompletableFuture<Object> cfr = CompletableFuture.supplyAsync(() -> {
            StringBuilder res1 = new StringBuilder();
            while (Optional.ofNullable(listOperations.size("test-list")).orElse(0L) > 0) {
                Object val = listOperations.rightPop("test-list", 5, TimeUnit.SECONDS);
                res1.append(val);
                log.info("brpop输出：{}", val);
            }
            return res1;
        }).thenApply((res) -> {
            log.info("取值结果：{}", res);
            return null;
        });
        cfr.join();
        // 删除键
//        log.info("删除键：{}", redisTemplate.delete("test-list"));
    }

    @Override
    public void redisSetOperation() {
        // redis set操作
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("test-set1", "rr", "aa", "bb", "cc", "dd");
        setOperations.add("test-set2", "aa", "cc", "ee", "ff", "ss", "ii");
        // 查询成员
        Set members = setOperations.members("test-set1");
        log.info("集合1成员：{}", members);
        // 移动集合成员
        setOperations.move("test-set2", "ii", "test-set1");
        // 查询成员
        members = setOperations.members("test-set1");
        log.info("集合1成员：{}", members);
        // 移除指定
        log.info("移除集合1中的ii:{}", setOperations.remove("test-set1", "ii"));
        // 随机成员
        log.info("集合1随机成员：{}", setOperations.randomMembers("test-set1", 15L));
        // 是否存在
        log.info("集合1中是否存在aa和jj：{}， {}", setOperations.isMember("test-set1", "aa"), setOperations.isMember("test-set2", "jj"));
        // diff
        log.info("集合1与集合2 diff：{}", setOperations.difference("test-set1", "test-set2"));
        // 交集
        log.info("集合1与集合2交集：{}", setOperations.intersect("test-set1", "test-set2"));
        // 并集存储
        setOperations.unionAndStore("test-set1", "test-set2", "test-set3");
        log.info("集合1与集合2并集并存储到集合3：{}", setOperations.pop("test-set3", 10L));
        // 删除键
        log.info("删除键：{}", redisTemplate.delete(List.of("test-set1", "test-set2", "test-set3")));
    }

    @Override
    public void redisZsetOperation() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        // 初始化ZSet
        Set<DefaultTypedTuple> set = new HashSet();
        set.add(new DefaultTypedTuple("a", 3.22));
        set.add(new DefaultTypedTuple("b", 2.937));
        set.add(new DefaultTypedTuple("c", 2.8873));
        set.add(new DefaultTypedTuple("d", 1.227));
        set.add(new DefaultTypedTuple("e", 3.182));
        set.add(new DefaultTypedTuple("f", 1.827));
        set.add(new DefaultTypedTuple("g", 2.1));
        zSetOperations.add("test-zset", set);
        // 成员
        log.info("ZSet成员：{}", zSetOperations.rangeByScoreWithScores("test-zset", 0, 9));
        // zset长度
        log.info("ZSet长度：{}", zSetOperations.zCard("test-zset"));
        // 区间成员数
        log.info("ZSet区间成员数：{}", zSetOperations.count("test-zset", 1.5, 3.3));
        // 成员分数增值
        log.info("ZSet成员增值：{}", zSetOperations.incrementScore("test-zset", "a", -1.2));
        // 排名
        log.info("ZSet中a的排名：{}", zSetOperations.rank("test-zset", "a"));
        // 移除指定成员
        log.info("ZSet中移除f成员：{}", zSetOperations.remove("test-zset", "f"));
        // 范围内成员数
        log.info("ZSet中2.1到3.2间的成员数：{}", zSetOperations.lexCount("test-zset", Range.open("2.1", "3.2")));
        log.info("ZSet中2.1到3.2间的成员数：{}", zSetOperations.lexCount("test-zset", Range.closed("2.1", "3.2")));
        // 删除键
        log.info("ZSet删除键：{}", redisTemplate.delete("test-zset"));
    }

    @Override
    public void redissonBloomFilter() {
        // 获取布隆过滤器
        RBloomFilter<Object> bloomFilterTest = redisson.getBloomFilter("bloomFilterTest");
        // 初始化
        bloomFilterTest.tryInit(10000L, 0.02);
        //
        redisson.getBucket("/api/v1/test").setIfAbsent("1", Duration.ofHours(1L));
        // 添加数据
        bloomFilterTest.add("/api/v1/getUser");
        bloomFilterTest.add("/api/v1/getInfo");
        // 判断数据
        log.info("是否存在getUser:{}", bloomFilterTest.contains("/api/v1/getUser"));
        log.info("是否存在getStudent:{}", bloomFilterTest.contains("/api/v1/getStudent"));

        RBlockingDeque<Object> myQueue = redisson.getBlockingDeque("my_queue");
        myQueue.subscribeOnFirstElements(message -> {
            return CompletableFuture.runAsync(() -> {
                log.info("接收消息：{}" ,message);
            });
        });
        myQueue.push("test queue 1");
        myQueue.push("test queue 2");
        myQueue.putAsync("test queue async");
    }

    @Override
    public void redisHyperOperation() {
        HyperLogLogOperations hyperLogLogOperations = redisTemplate.opsForHyperLogLog();
        hyperLogLogOperations.add("hyper-test", "140", "154", "144");
        redisTemplate.expire("hyper-test", 10, TimeUnit.SECONDS);
    }
}