package com.ruoyi.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.admin.domain.IdentifyResult;
import com.ruoyi.admin.service.IdentifyResultService;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.system.api.RemoteDictService;
import com.ruoyi.system.api.RemoteFileService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @GetMapping("/page")
    public TableDataInfo page(IdentifyResult identifyResult, @RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        logger.info("开始查询");
        Page<IdentifyResult> page = identifyResultService.getPage(identifyResult, pageNum, pageSize);
        return getDataTable(page);
    }

    @GetMapping("/{id}")
    public AjaxResult getIdentifyResult(@PathVariable("id") String id) {
        return success(identifyResultService.getById(id));
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

    /**
     * 模板上传
     *
     * @param file 模板文件
     * @return 模板上传结果
     */
    @PostMapping("/uploadTemplate")
    public AjaxResult uploadTemplate(@RequestParam("file") MultipartFile file) {

        return identifyResultService.uploadTemplate(file);

    }

    /**
     * 新增或更新
     *
     * @param identifyResult 数据
     * @return 结果
     */
    @PostMapping("")
    public AjaxResult saveOrUpdate(@RequestBody IdentifyResult identifyResult) {
        return identifyResultService.saveOrUpdate(identifyResult) ? success() : error();
    }

    /**
     * 删除数据
     *
     * @param ids 删除数据id
     * @return 删除结果
     */
    @DeleteMapping("")
    public AjaxResult delete(@RequestParam("ids") List<String> ids) {
        return identifyResultService.delCertificate(ids);
    }

    /**
     * 生成证书
     *
     * @param ids 数据ids
     * @return 生成结果
     */
    @PostMapping("/genCertificate")
    public AjaxResult genCertificate(@RequestParam("ids") List<String> ids) {
        return identifyResultService.genCertificate(ids);
    }

}
