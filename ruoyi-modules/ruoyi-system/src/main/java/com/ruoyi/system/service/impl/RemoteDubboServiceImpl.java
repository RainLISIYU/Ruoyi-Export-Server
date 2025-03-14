package com.ruoyi.system.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcException;
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
    @SentinelResource(value = "getInfo", fallback = "fallbackGetInfo")
    public String getInfo() {
        SysUser admin = sysUserService.selectUserByUserName("admin");
        String result = "Empty";
        if (! Objects.isNull(admin)) {
            result = admin.getUserName();
        }
        return result;
    }

    private String fallbackGetInfo() {
        return "sentinel调用异常";
    }
}
