package com.ruoyi.business.controller;/*
 *@Author:cq
 *@Date:2024/10/22 16:26
 */

import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lsy
 * @description es测试controller
 * @date 2024/10/22
 */
@RestController("/esTest")
public class ElasticsearchTestController {

    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;

}
