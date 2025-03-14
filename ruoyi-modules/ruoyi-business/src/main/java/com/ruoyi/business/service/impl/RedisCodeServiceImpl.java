package com.ruoyi.business.service.impl;

import com.ruoyi.business.service.RedisCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

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

    @Override
    public void redisStrOperation() {
        ValueOperations<String, Object> strOperations =
                redisTemplate.opsForValue();
        // 赋值并指定过期时间
        strOperations.set("str-test", "test", 10, TimeUnit.SECONDS);
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
    }

    @Override
    public void redisHashOperation() {

    }

    @Override
    public void redisListOperation() {

    }

    @Override
    public void redisSetOperation() {

    }

    @Override
    public void redisZsetOperation() {

    }
}
