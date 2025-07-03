package com.ruoyi;

import com.ruoyi.service.HelloService;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;

import java.util.ServiceLoader;

/*
 *@Author:cq
 *@Date:2024/4/30 17:31
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<HelloService> services = ServiceLoader.load(HelloService.class);
        for (HelloService service : services) {
            service.sayMyName();
        }
    }
}