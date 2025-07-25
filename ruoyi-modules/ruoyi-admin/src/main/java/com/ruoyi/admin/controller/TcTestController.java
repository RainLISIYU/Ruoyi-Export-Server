package com.ruoyi.admin.controller;

import com.ruoyi.admin.domain.TcTest;
import com.ruoyi.admin.service.TcTestService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.security.annotation.InnerAuth;
import com.ruoyi.system.api.domain.SysTcTest;
import io.seata.core.context.RootContext;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author lsy
 * @description 分布式事务测试controller
 * @date 2025/7/14
 */
@RestController
@RequestMapping("/tcTest")
@Slf4j
public class TcTestController {

    @Resource
    private TcTestService tcTestService;

    @PostMapping("/insert")
    @InnerAuth
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public R<Boolean> insert(@RequestBody SysTcTest tcTest) {
        tcTestService.saveOrUpdate(sysToTest(tcTest));
        boolean flag = true;
        log.info("当前事务xid:{}", RootContext.getXID());
        return R.ok(flag);
    }

    /**
     * 转换实体类
     *
     * @param sysTcTest 系统实体类
     * @return 实体类
     */
    private TcTest sysToTest(SysTcTest sysTcTest) {
        TcTest tcTest = new TcTest();
        tcTest.setId(sysTcTest.getId());
        tcTest.setName(sysTcTest.getName());
        tcTest.setAddress(sysTcTest.getAddress());
        return tcTest;
    }

}
