package com.ruoyi.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("save")
    @Operation(summary =  "工作流保存更新接口")
    @Parameters(value = {
            @Parameter(name = "name", description = "工作流名称"),
            @Parameter(name = "fileId", description = "工作流文件id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "保存成功")
    })
    public R<Boolean> save(@RequestBody SysWorkflow sysWorkflow) {
        if (ObjectUtils.isEmpty(sysWorkflow.getName()) || ObjectUtils.isEmpty(sysWorkflow.getFileId())) {
            return R.fail("参数异常");
        }
        return R.ok(sysWorkflowService.saveOrUpdate(sysWorkflow), "保存成功");
    }

    @DeleteMapping("delete")
    @Operation(summary =  "工作流删除接口")
    @Parameters(value = {
            @Parameter(name = "id", description = "工作流id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功")
    })
    public R<Boolean> delete(@RequestParam("ids") List<Long> ids) {
        LambdaQueryWrapper<SysWorkflow> queryWrapper = new LambdaQueryWrapper<SysWorkflow>().eq(SysWorkflow::getNeedDeploy, 1).in(SysWorkflow::getId, ids);
        List<SysWorkflow> list = sysWorkflowService.list(queryWrapper);
        if (! list.isEmpty()) {
            return R.fail("删除失败，存在正在使用的工作流");
        }
        return R.ok(sysWorkflowService.removeByIds(ids), "删除成功");
    }

    @PutMapping("deploy")
    @Operation(summary = "工作流部署")
    @Parameters(value = {
            @Parameter(name = "id", description = "工作流id")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "部署成功")
    })
    public R<Boolean> deploy(@RequestParam("id") Long id) {
        SysWorkflow workflow = sysWorkflowService.getById(id);
        if (ObjectUtils.isEmpty(workflow)) {
            return R.fail("工作流不存在");
        }
        if (workflow.getNeedDeploy() == 1) {
            return R.fail("工作流已部署");
        }
        return R.ok(sysWorkflowService.deploy(workflow), "部署成功");
    }

}
