package com.ruoyi.system.api;

import com.ruoyi.system.api.factory.RemoteBaiduAipFallbackFactory;
import feign.HeaderMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "remoteBaiduAipService", url = "https://iam.bj.baidubce.com", fallbackFactory = RemoteBaiduAipFallbackFactory.class)
public interface RemoteBaiduAipService {

    @GetMapping(value = "/v1/BCE-BEARER/token")
    String getToken(@HeaderMap Map<String, String> heads, @RequestParam("expireInSeconds") Long expireInSeconds);

}
