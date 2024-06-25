package com.ruoyi.business.controller;

import java.util.List;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.system.api.domain.SysTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.log.annotation.Log;
import com.ruoyi.common.log.enums.BusinessType;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.business.domain.MyTest;
import com.ruoyi.business.service.IMyTestService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.page.TableDataInfo;

/**
 * 测试Controller
 * 
 * @author ruoyi
 * @date 2024-03-14
 */
@RestController
@RequestMapping("/test")
public class MyTestController extends BaseController
{
    @Autowired
    private IMyTestService myTestService;

    /**
     * 查询测试列表
     */
    @RequiresPermissions("business:test:list")
    @GetMapping("/list")
    public TableDataInfo list(MyTest myTest) throws ExecutionException, InterruptedException {
        startPage();
        List<MyTest> list = myTestService.selectMyTestList(myTest).get();
        return getDataTable(list);
    }

    /**
     * 导出测试列表
     */
    @RequiresPermissions("business:test:export")
    @Log(title = "测试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MyTest myTest) throws ExecutionException, InterruptedException {
        List<MyTest> list = myTestService.selectMyTestList(myTest).get();
        ExcelUtil<MyTest> util = new ExcelUtil<MyTest>(MyTest.class);
        util.exportExcel(response, list, "测试数据");
    }

    /**
     * 获取测试详细信息
     */
    @RequiresPermissions("business:test:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(myTestService.selectMyTestById(id));
    }

    /**
     * 新增测试
     */
    @RequiresPermissions("business:test:add")
    @Log(title = "测试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MyTest myTest)
    {
        return toAjax(myTestService.insertMyTest(myTest));
    }

    /**
     * 修改测试
     */
    @RequiresPermissions("business:test:edit")
    @Log(title = "测试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MyTest myTest)
    {
        return toAjax(myTestService.updateMyTest(myTest));
    }

    /**
     * 删除测试
     */
    @RequiresPermissions("business:test:remove")
    @Log(title = "测试", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(myTestService.deleteMyTestByIds(ids));
    }

    /**
     * feign查询接口
     *
     * @return 结果集
     */
    @InnerAuth
    @GetMapping("query")
    public R<List<SysTest>> list(@RequestParam(value = "name") String name){
        List<MyTest> list = myTestService.list(new LambdaQueryWrapper<MyTest>().like(MyTest::getName, name));
        List<SysTest> collect = list.stream().map(item -> {
            SysTest test = new SysTest();
            test.setId(item.getId());
            test.setAddress(item.getAddress());
            test.setName(item.getName());
            return test;
        }).collect(Collectors.toList());
        int i = 1/0;
        return R.ok(collect);
    }

}
