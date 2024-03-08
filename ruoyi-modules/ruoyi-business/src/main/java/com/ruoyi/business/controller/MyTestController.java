package com.ruoyi.businesss.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
 * @date 2024-03-08
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
    @RequiresPermissions("system:test:list")
    @GetMapping("/list")
    public TableDataInfo list(MyTest myTest)
    {
        startPage();
        List<MyTest> list = myTestService.selectMyTestList(myTest);
        return getDataTable(list);
    }

    /**
     * 导出测试列表
     */
    @RequiresPermissions("system:test:export")
    @Log(title = "测试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MyTest myTest)
    {
        List<MyTest> list = myTestService.selectMyTestList(myTest);
        ExcelUtil<MyTest> util = new ExcelUtil<MyTest>(MyTest.class);
        util.exportExcel(response, list, "测试数据");
    }

    /**
     * 获取测试详细信息
     */
    @RequiresPermissions("system:test:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(myTestService.selectMyTestById(id));
    }

    /**
     * 新增测试
     */
    @RequiresPermissions("system:test:add")
    @Log(title = "测试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MyTest myTest)
    {
        return toAjax(myTestService.insertMyTest(myTest));
    }

    /**
     * 修改测试
     */
    @RequiresPermissions("system:test:edit")
    @Log(title = "测试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MyTest myTest)
    {
        return toAjax(myTestService.updateMyTest(myTest));
    }

    /**
     * 删除测试
     */
    @RequiresPermissions("system:test:remove")
    @Log(title = "测试", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(myTestService.deleteMyTestByIds(ids));
    }
}
