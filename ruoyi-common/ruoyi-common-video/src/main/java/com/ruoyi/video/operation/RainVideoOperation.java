package com.ruoyi.video.operation;

import com.ruoyi.video.property.RainVideoProperties;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

/**
 * @author lsy
 * @description 视频操作
 * @date 2025/7/3
 */
public class RainVideoOperation {

    private ThreadLocal<RainVideoProperties> videoProperties = new ThreadLocal<>();

    public void openCamera() {
        // 获取配置项
        RainVideoProperties rainVideoProperties = videoProperties.get();
        if (rainVideoProperties == null) {
            // 为空初始化
            rainVideoProperties = new RainVideoProperties();
            videoProperties.set(rainVideoProperties);
        }
        VideoCapture capture = new VideoCapture(0);
    }

}
