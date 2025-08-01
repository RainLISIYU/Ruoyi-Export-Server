package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.CertificateTemplate;
import com.ruoyi.admin.service.CertificateTemplateService;
import com.ruoyi.admin.mapper.CertificateTemplateMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class CertificateTemplateServiceImpl extends ServiceImpl<CertificateTemplateMapper, CertificateTemplate>
    implements CertificateTemplateService{

    @Override
    public CertificateTemplate getLastTemplate() {
        List<CertificateTemplate> list = this.list(new LambdaUpdateWrapper<CertificateTemplate>().orderByDesc(CertificateTemplate::getCreatedAt));
        if (list.isEmpty()) {
            return null;
        }
        return list.getFirst();
    }
}




