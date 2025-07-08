package com.ruoyi.video.configuration;

import com.ruoyi.video.property.RainVideoProperties;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description 视频处理配置类
 * @date 2025/7/3
 */
@Configuration
@EnableConfigurationProperties(RainVideoProperties.class)
public class RainVideoConfiguration {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("str1", "11");
        map.put("str3", "22");
        map.put("str2", "33");
        System.out.println(map.keySet());
    }

}
