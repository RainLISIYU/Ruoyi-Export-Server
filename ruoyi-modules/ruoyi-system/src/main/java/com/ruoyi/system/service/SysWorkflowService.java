package com.ruoyi.system.service;

import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.domain.SysWorkflow;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SysWorkflowService extends IService<SysWorkflow> {

    /**
     * 条件查询
     *
     * @param workflow 查询参数
     * @return 查询结果
     */
    TableDataInfo queryList(SysWorkflow workflow, String pi, String ps);
}
