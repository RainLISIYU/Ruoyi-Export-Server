package com.ruoyi.system.api.factory;

import com.ruoyi.system.api.RemoteBaiduAipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author lsy
 * @description 百度ai请求回调工厂
 * @date 2025/1/6
 */
@Component
public class RemoteBaiduAipFallbackFactory implements FallbackFactory<RemoteBaiduAipService> {

    private Logger logger = LoggerFactory.getLogger(RemoteBaiduAipFallbackFactory.class);

    @Override
    public RemoteBaiduAipService create(Throwable cause) {
        logger.error("百度ai调用失败:{}", cause.getMessage());
        return (name, source) -> "";
    }

}
