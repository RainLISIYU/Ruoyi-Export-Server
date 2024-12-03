package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.domain.SysWorkflow;
import com.ruoyi.system.service.SysWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lsy
 * @description 工作流配置Controller
 * @date 2024/11/28
 */
@RestController
@RequestMapping("/workflow")
@Tag(name = "SysWorkflowController", description = "工作流配置")
public class SysWorkflowController {

    @Resource
    private SysWorkflowService sysWorkflowService;

    @PostMapping("insert")
    public R<SysWorkflow> insert(@RequestBody SysWorkflow sysWorkflow) {
        return R.ok(null);
    }

    @GetMapping("query")
    @Operation(summary = "工作流查询接口")
    @Parameters(value = {
            @Parameter(name = "name", description = "工作流名称"),
            @Parameter(name = "file", description = "工作流文件")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "查询成功")
    })
    public TableDataInfo query(SysWorkflow workflow,
                               @Parameter(name = "pi", description = "页数") @RequestParam(defaultValue = "1", name = "pageNum") String pi,
                               @Parameter(name = "ps", description = "每页数量") @RequestParam(defaultValue = "10", name = "pageSize") String ps) {
        return sysWorkflowService.queryList(workflow, pi, ps);
    }

}
