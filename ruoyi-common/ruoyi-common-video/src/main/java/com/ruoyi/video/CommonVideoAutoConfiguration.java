package com.ruoyi.video;

import com.ruoyi.video.operation.RainVideoOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lsy
 * @description 视频处理自动配置类
 * @date 2025/7/3
 */
@Configuration
@ConditionalOnProperty(name = "common.video.enable", havingValue = "true", matchIfMissing = true)
public class CommonVideoAutoConfiguration {

    @Bean
    public RainVideoOperation rainVideoOperation() {
        return new RainVideoOperation();
    }

}
