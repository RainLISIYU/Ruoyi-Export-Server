package com.ruoyi.business.service;/*
 *@Author:cq
 *@Date:2025/3/13 17:21
 */

public interface RedisCodeService {

    /**
     * redis字符串操作
     */
    void redisStrOperation();

    /**
     * redis Hash操作
     */
    void redisHashOperation();

    /**
     * redis列表操作
     */
    void redisListOperation();

    /**
     * redis集合操作
     */
    void redisSetOperation();

    /**
     * redis有序集合操作
     */
    void redisZsetOperation();

    /**
     * redisson布隆过滤器测试
     */
    void redissonBloomFilter();

    /**
     * redis HyperLogLog操作
     */
    void redisHyperOperation();
}
