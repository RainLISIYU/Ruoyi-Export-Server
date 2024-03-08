package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.MyTest;

/**
 * 测试Service接口
 * 
 * @author ruoyi
 * @date 2024-03-08
 */
public interface IMyTestService 
{
    /**
     * 查询测试
     * 
     * @param id 测试主键
     * @return 测试
     */
    public MyTest selectMyTestById(Long id);

    /**
     * 查询测试列表
     * 
     * @param myTest 测试
     * @return 测试集合
     */
    public List<MyTest> selectMyTestList(MyTest myTest);

    /**
     * 新增测试
     * 
     * @param myTest 测试
     * @return 结果
     */
    public int insertMyTest(MyTest myTest);

    /**
     * 修改测试
     * 
     * @param myTest 测试
     * @return 结果
     */
    public int updateMyTest(MyTest myTest);

    /**
     * 批量删除测试
     * 
     * @param ids 需要删除的测试主键集合
     * @return 结果
     */
    public int deleteMyTestByIds(Long[] ids);

    /**
     * 删除测试信息
     * 
     * @param id 测试主键
     * @return 结果
     */
    public int deleteMyTestById(Long id);
}
