package com.ruoyi.business.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.domain.MovieEntity;
import com.ruoyi.business.service.MongoTestService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lsy
 * @description MongoDB测试服务层
 * @date 2025/4/7
 */
@Service("mongoTestService")
public class MongoTestServiceImpl implements MongoTestService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 保存
     *
     * @param movie 信息
     * @return 结果
     */
    @Override
    public R<String> saveMovie(MovieEntity movie) {
        MovieEntity save = mongoTemplate.save(movie);
        return R.ok("保存成功");
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

}
