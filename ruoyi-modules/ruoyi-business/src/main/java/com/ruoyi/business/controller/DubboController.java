package com.ruoyi.business.controller;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteDubboService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.model.LoginUser;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @author lsy
 * @description 调用Dubbo服务
 * @date 2025/2/17
 */
@RestController
@RequestMapping("/dubbo")
@Slf4j
public class DubboController {

    @DubboReference(loadbalance = "roundrobin")
    private RemoteDubboService remoteDubboService;

    @Resource
    private RemoteUserService remoteUserService;

    /**
     * Dubbo远程调用
     *
     * @return 结果
     */
    @GetMapping("/getInfo")
    public String getInfo() {
        return remoteDubboService.getInfo();
    }

    /**
     * Feign远程调用
     *
     * @return 结果
     */
    @GetMapping("/getUser")
    public String getUser() {
        R<LoginUser> userInfo = remoteUserService.getUserInfo("admin", SecurityConstants.INNER);
        LoginUser data = userInfo.getData();
        log.info(String.valueOf(data));
        if (! Objects.isNull(data)) {
            return data.getSysUser().getUserName();
        }
        return "Empty";
    }

}
