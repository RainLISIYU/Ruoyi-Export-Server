package com.ruoyi.video.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lsy
 * @description 视频处理配置项
 * @date 2025/7/3
 */
@Data
@ConfigurationProperties(prefix = "rain.video")
public class RainVideoProperties {

    public RainVideoProperties() {
        this.captureNum = "0";
        this.captureStatus = "1";
    }

    // 摄像头索引
    private String captureNum;

    // 摄像头状态 0 关 1 开
    private String captureStatus;

}
