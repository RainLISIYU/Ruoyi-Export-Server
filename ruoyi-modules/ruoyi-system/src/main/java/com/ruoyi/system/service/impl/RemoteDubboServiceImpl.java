package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Objects;

/**
 * @author lsy
 * @description Dubbo服务实现
 * @date 2025/2/17
 */
@DubboService
@Slf4j
public class RemoteDubboServiceImpl implements RemoteDubboService {

    @Resource
    private ISysUserService sysUserService;

    @Override
    public String getInfo() {
        SysUser admin = sysUserService.selectUserByUserName("admin");
        if (! Objects.isNull(admin)) {
            return admin.getUserName();
        }
        return "Empty";
    }
}
