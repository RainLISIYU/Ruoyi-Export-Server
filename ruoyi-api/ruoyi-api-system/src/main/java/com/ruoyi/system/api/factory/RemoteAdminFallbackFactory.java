package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteAdminService;
import com.ruoyi.system.api.domain.SysTcTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @author lsy
 * @description 测试模块降级类
 * @date 2025/7/14
 */
public class RemoteAdminFallbackFactory implements FallbackFactory<RemoteAdminService> {

    private final Logger logger = LoggerFactory.getLogger(RemoteAdminFallbackFactory.class);

    @Override
    public RemoteAdminService create(Throwable cause) {
        logger.error("测试服务调用失败:{}", cause.getMessage());
        return (tcTest, source) -> R.fail("远程调用失败");
    }
}
