package com.ruoyi.business.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbitmq.client.Channel;
import com.ruoyi.business.domain.MovieEntity;
import com.ruoyi.business.service.MongoTestService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.uuid.UUID;
import com.ruoyi.common.mq.configure.MovieRabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author lsy
 * @description MongoDB测试服务层
 * @date 2025/4/7
 */
@Service("mongoTestService")
@Slf4j
public class MongoTestServiceImpl implements MongoTestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 保存
     *
     * @param movie 信息
     * @return 结果
     */
    @Override
    public R<String> saveMovie(MovieEntity movie) {
        movie = mongoTemplate.save(movie);
        movieMqSend(movie);
        return R.ok("", "保存成功");
    }

    @Override
    public Page<MovieEntity> getMovies(int pageNum, int pageSize, MovieEntity movie) {
        // 排序
        // 分页
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        // 查询
        Query query = new Query();
        if (! StringUtils.isEmpty(movie.getTitle())) {
            query.addCriteria(Criteria.where("title").regex(movie.getTitle()));
        }
        if (! StringUtils.isEmpty(movie.getGenre())) {
            query.addCriteria(Criteria.where("genres").in(movie.getGenre()));
        }
        query.with(pageable);
        // 查询
        List<MovieEntity> movies = mongoTemplate.find(query, MovieEntity.class);
        // 返回结果
        Page<MovieEntity> page = new Page<>();
        page.setRecords(movies);
        page.setTotal(mongoTemplate.count(query, MovieEntity.class));
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        return page;
    }

    @Override
    public R<String> syncMovies() {
        // mongodb查询所有
        List<MovieEntity> movies = mongoTemplate.findAll(MovieEntity.class);
        // 删除索引
        elasticsearchTemplate.indexOps(MovieEntity.class).delete();
        // 同步到es
        for (MovieEntity movie : movies) {
            // 同步数据
            movieMqSend(movie);
        }
        return R.ok("", "同步成功");
    }

    private void movieMqSend(MovieEntity movie) {
        // 消息内容
        String msg = JSON.toJSONString(movie);
        // 消息配置
        MessageProperties properties = new MessageProperties();
        properties.setExpiration("6000");
        // 消息实体
        Message message = MessageBuilder
                .withBody(msg.getBytes())
                .andProperties(properties)
                .setCorrelationId(UUID.randomUUID().toString().replace("-", ""))
                .build();
        // 发送
        rabbitTemplate.convertAndSend(MovieRabbitConfig.MOVIE_EXCHANGE, MovieRabbitConfig.MOVIE_ROUTING, message);
    }

    @RabbitListener(queues = MovieRabbitConfig.MOVIE_QUEUE)
    public void movieMqReceive(Channel channel, Message message) throws IOException {
        String msgBody = new String(message.getBody());
        MovieEntity movieEntity;
        try {
            movieEntity = JSON.parseObject(msgBody, MovieEntity.class);
        } catch (Exception e) {
            log.error("消息转换失败：{}", msgBody);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            return;
        }
        if (movieEntity == null) {
            log.error("消息转换失败：{}", msgBody);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
        }
        log.info("Movie消息接收成功：{}", msgBody);
        esSaveMovie(movieEntity);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    private void esSaveMovie(MovieEntity movieEntity) {
        // 查询索引
        IndexOperations movieIndex = elasticsearchTemplate.indexOps(MovieEntity.class);
        if (! movieIndex.exists()) {
            synchronized (MovieEntity.class) {
                if (! movieIndex.exists()) {
                    // 不存在则创建
                    movieIndex.create();
                    // 添加映射
//                    elasticsearchTemplate.indexOps(MovieEntity.class).putMapping(MovieEntity.class);
                }
            }

        }
        // 保存文档
        MovieEntity save = elasticsearchTemplate.save(movieEntity);
        log.info("es同步成功：{}", JSON.toJSONString(save));
    }

}
