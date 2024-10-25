package com.ruoyi.es;/*
 *@Author:cq
 *@Date:2024/10/18 17:39
 */

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * @author lsy
 * @description 测试
 * @date 2024/10/18
 */
@Document(indexName = "test")
public class ElasticSearchTestConfig {

    private ElasticsearchConfiguration elasticsearchConfiguration;

}
