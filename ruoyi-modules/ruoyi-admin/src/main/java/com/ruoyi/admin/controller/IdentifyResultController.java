package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.admin.domain.IdentifyResult;
import com.ruoyi.admin.service.IdentifyResultService;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.context.TraceIdContext;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.api.RemoteDictService;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysFile;
import jakarta.annotation.Resource;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lsy
 * @description 检定Controller
 * @date 2025/7/28
 */
@RestController
@RequestMapping("/api/v1/verify")
public class IdentifyResultController extends BaseController {

    @Resource
    private IdentifyResultService identifyResultService;

    @Resource
    private RemoteFileService remoteFileService;

    @Resource
    private RemoteDictService remoteDictService;

    /**
     * 分页查询
     *
     * @param identifyResult 条件
     * @param pageNum 页号
     * @param pageSize 页大小
     * @return 查询结果
     */
    @RequestMapping("/page")
    public TableDataInfo page(IdentifyResult identifyResult, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        logger.info("开始查询");
        Page<IdentifyResult> page = identifyResultService.getPage(identifyResult, pageNum, pageSize);
        return getDataTable(page);
    }

    /**
     * 导入数据
     *
     * @param file 文件
     * @return 数据
     */
    @PostMapping("/importData")
    public AjaxResult importData(@RequestParam("file") MultipartFile[] file) {

        return identifyResultService.importData(file);
    }

}
