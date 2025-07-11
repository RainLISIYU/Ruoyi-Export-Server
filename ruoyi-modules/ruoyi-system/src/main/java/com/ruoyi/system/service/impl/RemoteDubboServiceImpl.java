package com.ruoyi.system.service.impl;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ruoyi.common.core.context.SecurityContextHolder;
import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.domain.SysUser;
import com.ruoyi.system.api.filter.UserInfoGetFilter;
import com.ruoyi.system.service.ISysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.redisson.Redisson;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author lsy
 * @description Dubbo服务实现
 * @date 2025/2/17
 */
@DubboService(retries = 0, timeout = 3000)
@Slf4j
@Service
public class RemoteDubboServiceImpl implements RemoteDubboService {

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private Redisson redisson;

    @Override
    @SentinelResource(value = "getInfo")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getInfo() {
        log.info("Dubbo远程调用线程：" + Thread.currentThread() + ",当前用户：" + UserInfoGetFilter.getUsername());
        SysUser admin = sysUserService.selectUserByUserName("admin");
        SysUser newUser = new SysUser();
        newUser.setUserName("事务测试");
        newUser.setNickName("测试");
        sysUserService.insertUser(newUser);
        boolean flag = true;
        if (flag) {
            int i = 1 / 0;
        }
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
