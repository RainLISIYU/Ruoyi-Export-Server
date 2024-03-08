package com.ruoyi.business.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.business.entity.MyTest;
import com.ruoyi.business.service.MyTestService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 测试表(MyTest)表控制层
 *
 * @author chenqiang
 * @since 2024-03-08 14:08:04
 */
@RestController
@RequestMapping("myTest")
public class MyTestController extends BaseController {
    /**
     * 服务对象
     */
    @Resource
    private MyTestService myTestService;

    /**
     * 分页查询所有数据
     *
     * @param page   分页对象
     * @param myTest 查询实体
     * @return 所有数据
     */
    @GetMapping
    public AjaxResult selectAll(Page<MyTest> page, MyTest myTest) {
        return success(this.myTestService.page(page, new QueryWrapper<>(myTest)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public AjaxResult selectOne(@PathVariable Serializable id) {
        return success(this.myTestService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param myTest 实体对象
     * @return 新增结果
     */
    @PostMapping
    public AjaxResult insert(@RequestBody MyTest myTest) {
        return success(this.myTestService.save(myTest));
    }

    /**
     * 修改数据
     *
     * @param myTest 实体对象
     * @return 修改结果
     */
    @PutMapping
    public AjaxResult update(@RequestBody MyTest myTest) {
        return success(this.myTestService.updateById(myTest));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public AjaxResult delete(@RequestParam("idList") List<Long> idList) {
        return success(this.myTestService.removeByIds(idList));
    }
}

