package com.ruoyi.business.test;

import com.ruoyi.business.service.MockWeatherService;
import com.ruoyi.common.core.utils.http.HttpUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author lsy
 * @description 嵌入模型测试
 * @date 2025/2/24
 */
@SpringBootTest
@Slf4j
public class EmbeddingTest {

    private final EmbeddingModel embeddingModel;

    @Resource
    private MockWeatherService mockWeatherService;

    @Autowired
    public EmbeddingTest(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Test
    public void test() {
        EmbeddingResponse response = embeddingModel.call(new EmbeddingRequest(List.of("你好", "世界"), OllamaOptions.builder().build()));
        log.info("embedding: {}", response.getResults());
    }

    @Test
    public void weatherTest() {
        MockWeatherService.Request request = new MockWeatherService.Request("太原", "清徐");
        MockWeatherService.Response response = mockWeatherService.apply(request);
        log.info("{}", response);
    }

}
