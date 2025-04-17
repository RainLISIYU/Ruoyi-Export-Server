package com.ruoyi.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.domain.MovieEntity;
import com.ruoyi.common.core.domain.R;

public interface MongoTestService {

    /**
     * 保存电影信息
     *
     * @param movie 电影信息
     * @return 结果
     */
    R<String> saveMovie(MovieEntity movie);

    /**
     * 查询电影信息
     *
     * @param movie 查询条件
     * @return 电影信息集合
     */
    Page<MovieEntity> getMovies(int pageNum, int pageSize, MovieEntity movie);

    /**
     * 同步数据
     *
     * @return 同步结果
     */
    R<String> syncMovies();
}
