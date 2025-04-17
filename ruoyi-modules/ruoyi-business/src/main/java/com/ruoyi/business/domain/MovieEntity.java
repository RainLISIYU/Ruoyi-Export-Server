package com.ruoyi.business.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author lsy
 * @description 电影实体类
 * @date 2025/4/7
 */
@Document(collection = "movies")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "movies")
@Data
public class MovieEntity {

    /**
     * 文档id
     */
    @Id
    @Field(type = FieldType.Text, index = false)
    private String id;

    /**
     * 标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_smart")
    private String title;

    /**
     * 体裁
     */
    @Field(type = FieldType.Text)
    private List<String> genres;

    /**
     * 时长
     */
    @Field(type = FieldType.Integer)
    private Integer runtime;

    /**
     * 导演
     */
    @Field(type = FieldType.Nested)
    private MovieDirectorEntity director;

    /**
     * 评级
     */
    @Field(type = FieldType.Text)
    private String rated;

    /**
     * 年份
     */
    @Field(type = FieldType.Integer)
    private Integer year;

    /**
     * 上传文件ids
     */
    @Field(type = FieldType.Text, index = false)
    private String fileIds;

    /**
     * 类型查询条件
     */
    @Transient
    private String genre;

}
