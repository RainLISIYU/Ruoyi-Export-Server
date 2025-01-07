package com.ruoyi.system.api.factory;/*
 *@Author:cq
 *@Date:2024/3/15 16:47
 */

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteBusinessService;
import com.ruoyi.system.api.domain.SysTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lsy
 * @description 开发异常回调
 * @date 2024/3/15
 */
@Component
public class RemoteBusinessFallbackFactory implements FallbackFactory<RemoteBusinessService> {

    private static final Logger logger = LoggerFactory.getLogger(RemoteBusinessFallbackFactory.class);

    @Override
    public RemoteBusinessService create(Throwable cause) {

        logger.error("开发服务调用失败:{}", cause.getMessage());
        return (name, source) -> R.fail("测试查询失败：" + cause.getMessage());
    }
}
