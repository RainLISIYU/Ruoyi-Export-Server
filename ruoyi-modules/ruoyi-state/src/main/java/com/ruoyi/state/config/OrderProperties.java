package com.ruoyi.state.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lsy
 * @description 测试配置
 * @date 2024/4/10
 */

@Component
@Data
@RefreshScope
@ConfigurationProperties(prefix = "state.order")
public class OrderProperties {

    private String name;

}
