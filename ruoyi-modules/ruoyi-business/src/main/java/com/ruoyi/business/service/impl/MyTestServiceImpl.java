package com.ruoyi.business.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.mapper.MyTestMapper;
import com.ruoyi.business.domain.MyTest;
import com.ruoyi.business.service.IMyTestService;

/**
 * 测试Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-03-14
 */
@Service
public class MyTestServiceImpl extends ServiceImpl<MyTestMapper, MyTest> implements IMyTestService
{
    @Autowired
    private MyTestMapper myTestMapper;

    /**
     * 查询测试
     * 
     * @param id 测试主键
     * @return 测试
     */
    @Override
    public MyTest selectMyTestById(Long id)
    {
        return myTestMapper.selectById(id);
    }

    /**
     * 查询测试列表
     * 
     * @param myTest 测试
     * @return 测试
     */
    @Override
    public List<MyTest> selectMyTestList(MyTest myTest)
    {
//        return myTestMapper.selectMyTestList(myTest);
        return this.list();
    }

    /**
     * 新增测试
     * 
     * @param myTest 测试
     * @return 结果
     */
    @Override
    public int insertMyTest(MyTest myTest)
    {
        return myTestMapper.insertMyTest(myTest);
    }

    /**
     * 修改测试
     * 
     * @param myTest 测试
     * @return 结果
     */
    @Override
    public int updateMyTest(MyTest myTest)
    {
//        return myTestMapper.updateMyTest(myTest);
        return myTestMapper.updateById(myTest);
    }

    /**
     * 批量删除测试
     * 
     * @param ids 需要删除的测试主键
     * @return 结果
     */
    @Override
    public int deleteMyTestByIds(Long[] ids)
    {
//        return myTestMapper.deleteMyTestByIds(ids);
        return this.removeByIds(Arrays.asList(ids)) ? 1 : 0;
    }

    /**
     * 删除测试信息
     * 
     * @param id 测试主键
     * @return 结果
     */
    @Override
    public int deleteMyTestById(Long id)
    {
        return myTestMapper.deleteMyTestById(id);
    }
}
