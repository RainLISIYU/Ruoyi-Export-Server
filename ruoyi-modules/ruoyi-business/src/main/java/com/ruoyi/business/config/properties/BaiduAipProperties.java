package com.ruoyi.business.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsy
 * @description 百度AI配置类
 * @date 2025/1/6
 */
@Configuration
@ConfigurationProperties(prefix = "baidu.aip")
@Data
public class BaiduAipProperties {

    /**
     * ak
     */
    private String accessKey;

    /**
     * sk
     */
    private String secretKey;

    /**
     * AppId
     */
    private String appId;

    /**
     * 过期时间 秒
     */
    private String expirationPeriodInSeconds;

    /**
     * 模型名称
     */
    private String model;

    /**
     * 认证前缀
     */
    private String authPrefix;

}
