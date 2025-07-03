package com.ruoyi.service.impl;

import com.ruoyi.service.HelloService;

/**
 * @author lsy
 * @description SPI测试
 * @date 2025/6/10
 */
public class WorldServiceImpl implements HelloService {

    @Override
    public void sayMyName()
    {
        System.out.println("WorldServiceImpl");
    }

}
