package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.domain.SysWorkflow;
import com.ruoyi.system.service.SysWorkflowService;
import com.ruoyi.system.mapper.SysWorkflowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class SysWorkflowServiceImpl extends ServiceImpl<SysWorkflowMapper, SysWorkflow>
    implements SysWorkflowService{

    @Override
    public TableDataInfo queryList(SysWorkflow workflow, String pi, String ps) {
        String name = workflow.getName();
        LambdaQueryWrapper<SysWorkflow> queryWrapper = new LambdaQueryWrapper<SysWorkflow>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(SysWorkflow::getName, name);
        }
        return getTableData(this.page(new Page<>(Long.parseLong(pi), Long.parseLong(ps)), queryWrapper));
    }

    /**
     * 封装结果
     *
     * @param page 分页查询结果
     * @return
     */
    private TableDataInfo getTableData(Page<?> page) {
        TableDataInfo tableDataInfo = new TableDataInfo();
        tableDataInfo.setCode(HttpStatus.SUCCESS);
        tableDataInfo.setRows(page.getRecords());
        tableDataInfo.setTotal(page.getTotal());
        return tableDataInfo;
    }
}




