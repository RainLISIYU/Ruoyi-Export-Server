package com.ruoyi.admin.service;

import com.ruoyi.admin.domain.CertificateTemplate;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CertificateTemplateService extends IService<CertificateTemplate> {

    /**
     * 获取最新的模板
     *
     * @return 模板信息
     */
    CertificateTemplate getLastTemplate();

}
