//package com.ruoyi.business.test;/*
// *@Author:cq
// *@Date:2024/10/22 16:34
// */
//
//import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
//import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
//import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
//import com.ruoyi.business.domain.ElasticTest;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
//import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.client.elc.NativeQuery;
//import org.springframework.data.elasticsearch.core.AggregationsContainer;
//import org.springframework.data.elasticsearch.core.IndexOperations;
//import org.springframework.data.elasticsearch.core.SearchHit;
//import org.springframework.data.elasticsearch.core.SearchHits;
//import org.springframework.data.elasticsearch.core.query.Query;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author lsy
// * @description es测试类
// * @date 2024/10/22
// */
//@SpringBootTest
//@Slf4j
//public class EsTest {
//
//    @Resource
//    private ElasticsearchTemplate elasticsearchTemplate;
//
//    /**
//     * 判断索引是否存在
//     */
//    @Test
//    public void existIndex() {
//        IndexOperations indexOperations = elasticsearchTemplate.indexOps(ElasticTest.class);
//        boolean exists = indexOperations.exists();
//        System.out.println(exists);
//    }
//
//    /**
//     * 创建索引
//     */
//    @Test
//    public void createIndex() {
//        IndexOperations indexOperations = elasticsearchTemplate.indexOps(ElasticTest.class);
//        // 索引存在则删除
//        if (indexOperations.exists()) {
//            indexOperations.delete();
//        }
//        // 创建索引
//        indexOperations.create();
//        log.info("测试索引创建成功");
//        // 添加映射
//        elasticsearchTemplate.indexOps(ElasticTest.class).putMapping();
//    }
//
//    /**
//     * 删除索引
//     */
//    @Test
//    public void deleteIndex() {
//        IndexOperations indexOperations = elasticsearchTemplate.indexOps(ElasticTest.class);
//        boolean delete = indexOperations.delete();
//        log.info("删除索引" + delete);
//    }
//
//    /**
//     * 保存文档
//     */
//    @Test
//    public void insertDocument() {
//        // 文档实体
//        ElasticTest dto = ElasticTest.builder()
//                .id(1L)
//                .title("这是测试文档的标题。")
//                .description("测试文档描述：这是Elasticsearch的文档描述。")
//                .createTime(LocalDateTime.now())
//                .duration(100).build();
//        // 保存
//        ElasticTest save = elasticsearchTemplate.save(dto);
//        log.info("保存成功：{}", save);
//    }
//
//    /**
//     * 更新文档
//     */
//    @Test
//    public void updateDocument() {
//        // 文档实体
//        ElasticTest dto = ElasticTest.builder()
//                .id(1L)
//                .title("这是测试文档的标题。")
//                .description("测试文档描述：这是Elasticsearch的文档描述。")
//                .createTime(LocalDateTime.now())
//                .category("更新")
//                .duration(102).build();
//        // 更新
//        ElasticTest save = elasticsearchTemplate.save(dto);
//        log.info("更新成功：{}",save);
//    }
//
//    /**
//     * 批量新增
//     */
//    @Test
//    public void batchInsert() {
//        List<ElasticTest> list = new ArrayList<>();
//        list.add(ElasticTest.builder()
//                .id(4L)
//                .title("王炯地哦为日瓦日温柔。")
//                .description("测试文档描述：这是Elasticsearch的文档描述。")
//                .createTime(LocalDateTime.now())
//                .category("批量新增")
//                .duration(100).build());
//        list.add(ElasticTest.builder()
//                .id(10L)
//                .title("你发的开始暖风机阿斯顿发。")
//                .description("测试文档描述：这是Elasticsearch的文档描述。")
//                .createTime(LocalDateTime.now())
//                .category("批量新增")
//                .duration(100).build());
//        list.add(ElasticTest.builder()
//                .id(12L)
//                .title("你发的开始暖风机阿斯顿发。")
//                .description("测试文档描述：这是Elasticsearch的文档描述。")
//                .createTime(LocalDateTime.now())
//                .category("批量新增")
//                .duration(100).build());
//        // 批量新增
//        Iterable<ElasticTest> save = elasticsearchTemplate.save(list);
//        log.info("批量新增成功：{}", save);
//    }
//
//    /**
//     * 主键查询
//     */
//    @Test
//    public void searchById() {
//        ElasticTest byId = elasticsearchTemplate.get("1", ElasticTest.class);
//        log.info("查询成功：{}", byId);
//    }
//
//    /**
//     * 根据主键删除
//     */
//    @Test
//    public void deleteById() {
//        String delete = elasticsearchTemplate.delete("2", ElasticTest.class);
//        log.info("删除成功：{}", delete);
//    }
//
//    /**
//     * 查询所有
//     */
//    @Test
//    public void searchAll() {
//        SearchHits<ElasticTest> search = elasticsearchTemplate.search(Query.findAll(), ElasticTest.class);
//        List<SearchHit<ElasticTest>> searchHits = search.getSearchHits();
//        // 获取实体
//        List<ElasticTest> list = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            list.add(hit.getContent());
//        });
//        log.info("查询所有：{}", list);
//    }
//
//    /**
//     * 条件查询
//     */
//    @Test
//    public void matchQuery() {
//        // 条件查询
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q.match(m -> m.field("category").query("批量新增")))
//                .withSort(Sort.by("id").ascending()).build();
//        // 获取searchHits
//        List<SearchHit<ElasticTest>> searchHits = elasticsearchTemplate.search(query, ElasticTest.class).getSearchHits();
//        // 获取实体
//        List<ElasticTest> list = new ArrayList<>();
//        searchHits.forEach(hit -> {
//            list.add(hit.getContent());
//        });
//        log.info("条件查询：{}", list);
//    }
//
//    /**
//     * 分页查询
//     */
//    @Test
//    public void pageSearch() {
//        // 条件
//        Query query = NativeQuery.builder()
//                .withQuery(q -> q.match(m -> m.field("description").query("测试文档")))
//                .withPageable(Pageable.ofSize(1).withPage(1)).build();
//        // 查询
//        SearchHits<ElasticTest> search = elasticsearchTemplate.search(query, ElasticTest.class);
//        // 获取查询结果
//        List<ElasticTest> list = new ArrayList<>();
//        search.forEach(hit -> {
//            list.add(hit.getContent());
//        });
//        log.info("分页查询：{}", list);
//    }
//
//    /**
//     * 聚合查询
//     */
//    @Test
//    public void aggQuery() {
//        // 聚合查询条件
//        Query query = NativeQuery.builder()
//                .withAggregation("category_group", Aggregation.of(a -> a.terms(te -> te.field("category").size(2))))
//                .build();
//        // 查询
//        SearchHits<ElasticTest> search = elasticsearchTemplate.search(query, ElasticTest.class);
//        // 获取聚合数据
//        ElasticsearchAggregations aggregationsContainer = (ElasticsearchAggregations) search.getAggregations();
//        Map<String, ElasticsearchAggregation> aggregations =
//                Objects.requireNonNull(aggregationsContainer).aggregationsAsMap();
//        // 获取对应名称聚合数据
//        ElasticsearchAggregation categoryGroup = aggregations.get("category_group");
//        Buckets<StringTermsBucket> buckets = categoryGroup.aggregation().getAggregate().sterms().buckets();
//        // 打印
//        buckets.array().forEach(bucket -> {
//            System.out.println("组名:" + bucket.key().stringValue() + ", 值：" + bucket.docCount());
//        });
//        // 获取searchHits信息
//        List<ElasticTest> list = new ArrayList<>();
//        search.forEach(hit -> {
//            list.add(hit.getContent());
//        });
//        log.info("聚合查询：{}", list);
//        Map<String, String> map = new HashMap<>();
//        Map<String, String> table = new Hashtable<>();
//        Map<String, String> cmap = new ConcurrentHashMap<>();
//
//    }
//}
