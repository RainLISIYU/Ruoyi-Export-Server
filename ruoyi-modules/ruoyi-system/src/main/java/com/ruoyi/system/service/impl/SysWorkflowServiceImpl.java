package com.ruoyi.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysFile;
import com.ruoyi.system.domain.SysWorkflow;
import com.ruoyi.system.service.SysWorkflowService;
import com.ruoyi.system.mapper.SysWorkflowMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class SysWorkflowServiceImpl extends ServiceImpl<SysWorkflowMapper, SysWorkflow>
    implements SysWorkflowService{

    @Resource
    private RemoteFileService remoteFileService;

    @Override
    public TableDataInfo queryList(SysWorkflow workflow, String pi, String ps) {
        String name = workflow.getName();
        LambdaQueryWrapper<SysWorkflow> queryWrapper = new LambdaQueryWrapper<SysWorkflow>();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like(SysWorkflow::getName, name);
        }
        Page<SysWorkflow> page = this.page(new Page<>(Long.parseLong(pi), Long.parseLong(ps)), queryWrapper);
        List<SysWorkflow> records = page.getRecords();
        // 获取工作流文件信息
        Set<Long> fileIds = records.stream().map(SysWorkflow::getFileId).collect(Collectors.toSet());
        fileIds.addAll(records.stream().map(SysWorkflow::getImgId).collect(Collectors.toSet()));
        if (! fileIds.isEmpty()) {
            List<SysFile> sysFiles = remoteFileService.listFiles(fileIds, SecurityConstants.INNER).getData();
            Map<Long, SysFile> fileIdMap = new HashMap<>();
            if (!ObjectUtils.isEmpty(sysFiles) && ! sysFiles.isEmpty()) {
                fileIdMap = sysFiles.stream().collect(Collectors.toMap(SysFile::getId, sysFile -> sysFile));
            }
            // 设置文件信息
            for (SysWorkflow sysWorkflow : records) {
                SysFile sysFile = fileIdMap.get(sysWorkflow.getFileId());
                if (! ObjectUtils.isEmpty(sysFile)) {
                    sysWorkflow.setFilePath(sysFile.getUrl());
                    sysWorkflow.setFileName(sysFile.getName());
                }
                sysFile = fileIdMap.get(sysWorkflow.getImgId());
                if (! ObjectUtils.isEmpty(sysFile)) {
                    sysWorkflow.setImgPath(sysFile.getUrl());
                    sysWorkflow.setImgName(sysFile.getName());
                }
            }
        }
        return getTableData(page);
    }

    @Override
    public Boolean deploy(SysWorkflow workflow) {
        return null;
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




