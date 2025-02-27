package com.ruoyi.business.ai.configuration;

import com.ruoyi.business.service.MockWeatherService;
import com.ruoyi.business.service.impl.MockWeatherServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import java.util.function.Function;

/**
 * @author lsy
 * @description ai工具配置类
 * @date 2025/2/25
 */
@Configuration
public class FunctionCallConfiguration {

    @Bean
    @Description("查询某个城市当天天气")
    public Function<MockWeatherService.Request, MockWeatherService.Response> getWeatherFunction(MockWeatherService mockWeatherService) {
        return mockWeatherService;
    }

}
