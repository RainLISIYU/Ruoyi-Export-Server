package com.ruoyi.system.api.factory;

import com.ruoyi.system.api.RemoteDubboService;

/**
 * @author lsy
 * @description Dubbo远程调用降级
 * @date 2025/3/14
 */
public class RemoteDubboServiceMock implements RemoteDubboService {
    @Override
    public String getInfo() {
        return "远程调用失败";
    }
}
