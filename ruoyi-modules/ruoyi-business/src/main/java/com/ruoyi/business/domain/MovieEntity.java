package com.ruoyi.business.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

/**
 * @author lsy
 * @description 电影实体类
 * @date 2025/4/7
 */
@Document(collection = "movies")
@Data
public class MovieEntity {

    /**
     * 文档id
     */
    @Id
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 体裁
     */
    private List<String> genres;

    /**
     * 时长
     */
    private Integer runtime;

    /**
     * 导演
     */
    private MovieDirectorEntity director;

    /**
     * 评级
     */
    private String rated;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 上传文件ids
     */
    private String fileIds;

    /**
     * 类型查询条件
     */
    @Transient
    private String genre;

}
