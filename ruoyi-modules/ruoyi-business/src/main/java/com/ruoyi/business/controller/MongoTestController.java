package com.ruoyi.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.domain.MovieEntity;
import com.ruoyi.business.service.MongoTestService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author lsy
 * @description MongoDB测试类
 * @date 2025/4/7
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/mongo")
public class MongoTestController {

    private final MongoTestService mongoTestService;

    MongoTestController(MongoTestService mongoTestService) {
        this.mongoTestService = mongoTestService;
    }

    /**
     * 保存信息
     *
     * @param movie 信息
     * @return 结果
     */
    @PostMapping("save")
    public R<String> saveMovie(@RequestBody MovieEntity movie) {
        if (StringUtils.isEmpty(movie.getId())) {
            movie.setId(null);
        }
        try {
            return mongoTestService.saveMovie(movie);
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }

    /**
     * 分页查询
     *
     * @param pageNum 页号
     * @param pageSize 每页大小
     * @param movie 查询参数
     * @return 查询结果
     */
    @GetMapping("getMovies")
    public R<Page<MovieEntity>> getMovies(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize, MovieEntity movie) {
        try {
            return R.ok(mongoTestService.getMovies(pageNum, pageSize, movie));
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

    @GetMapping("syncMovies")
    public R<String> syncMovies() {
        try {
            return mongoTestService.syncMovies();
        } catch (Exception e) {
            log.error(e.getMessage());
            return R.fail(e.getMessage());
        }
    }

}
