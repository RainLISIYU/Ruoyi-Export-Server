package com.ruoyi.business.controller;

import com.ruoyi.business.config.BaiduAipUtils;
import com.ruoyi.system.api.RemoteBaiduAipService;
import jakarta.annotation.Resource;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lsy
 * @description 百度AI请求Controller
 * @date 2025/1/6
 */
@RestController
@RequestMapping("/baiduAip")
public class BaiduAipController {

    @Resource
    private RemoteBaiduAipService remoteBaiduAipService;

    @Resource
    private BaiduAipUtils baiduAipUtils;

    public String getToken() {
        String method = "GET";
        String path = "/v1/BCE-BEARER/token";
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "iam.bj.baidubce.com");
        headers.put("Content-Type", "application/json");
        String authorizationString = baiduAipUtils.generateSignKey(method, path, headers);
        headers.put("Authorization", authorizationString);
        return remoteBaiduAipService.getToken(headers, 2000L);
    }

}
