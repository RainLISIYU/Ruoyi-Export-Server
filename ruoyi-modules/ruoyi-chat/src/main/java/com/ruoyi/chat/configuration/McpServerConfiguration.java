package com.ruoyi.chat.configuration;

import com.ruoyi.chat.service.OpenMeteoService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;

/**
 * @author lsy
 * @description Mcp服务端配置类
 * @date 2025/5/15
 */
@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider weatherTools(OpenMeteoService meteoService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(meteoService)
                .build();
    }

}
