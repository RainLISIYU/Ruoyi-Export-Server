package com.ruoyi.chat;

import com.ruoyi.chat.service.OpenMeteoService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author lsy
 * @description 天气接口测试
 * @date 2025/5/15
 */
@SpringBootTest
public class OpenMetaTest {

    @Resource
    private OpenMeteoService openMeteoService;

    @Test
    public void openMeteoServiceTest() {
        System.out.println(openMeteoService.getWeatherForecastByLocation("37.8077", "112.5681"));
    }
}
