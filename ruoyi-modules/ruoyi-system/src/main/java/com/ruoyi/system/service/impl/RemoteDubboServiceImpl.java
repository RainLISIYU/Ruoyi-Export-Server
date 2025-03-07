package com.ruoyi.system.service.impl;

import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.Redisson;
import org.redisson.api.RLock;

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

    @Resource
    private Redisson redisson;

    @Override
    public String getInfo() {
//        RLock adminLock = redisson.getLock("admin");
        SysUser admin = sysUserService.selectUserByUserName("admin");
        if (! Objects.isNull(admin)) {
//            adminLock.lock();
            return admin.getUserName();
        }
//        adminLock.unlock();
        return "Empty";
    }
}
