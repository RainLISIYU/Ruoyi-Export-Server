package com.ruoyi.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.admin.domain.IdentifyResult;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.common.core.web.domain.AjaxResult;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
public interface IdentifyResultService extends IService<IdentifyResult> {

    /**
     * 分页查询
     *
     * @param identifyResult 查询参数
     * @param pi 页号
     * @param ps 页大小
     * @return 查询结果
     */
    Page<IdentifyResult> getPage(IdentifyResult identifyResult, Integer pi, Integer ps);

    /**
     * 导入数据
     *
     * @param file 导入文件
     * @return 导入结果
     */
    AjaxResult importData(MultipartFile[] file);

    /**
     * 模板上传
     *
     * @param file 文件
     * @return 上传结果
     */
    AjaxResult uploadTemplate(MultipartFile file);

}
