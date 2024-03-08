package com.ruoyi.business.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.ruoyi.business.entity.MyTest;
import org.springframework.stereotype.Component;

/**
 * 测试表(MyTest)表数据库访问层
 *
 * @author chenqiang
 * @since 2024-03-08 14:08:04
 */
@Mapper
public interface MyTestDao extends BaseMapper<MyTest> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyTest> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<MyTest> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<MyTest> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<MyTest> entities);

}

