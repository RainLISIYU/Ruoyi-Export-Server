package com.ruoyi.system.api;

import com.ruoyi.system.api.factory.RemoteBaiduAipFallbackFactory;
import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "remoteBaiduAipService", url = "https://baidu.com", fallbackFactory = RemoteBaiduAipFallbackFactory.class)
public interface RemoteBaiduAipService {

    @GetMapping(value = "/v1/BCE-BEARER/token")
    String getToken(@HeaderMap Map<String, String> heads, @RequestParam("expireInSeconds") Long expireInSeconds);

    @PostMapping(value = "/search")
    String getSearch(@RequestHeader Map<String, Object> headers, @RequestBody Map<String, Object> params);

}
