package com.ruoyi.state.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.constant.HttpStatus;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.web.controller.BaseController;
import com.ruoyi.common.core.web.page.TableDataInfo;
import com.ruoyi.common.datasource.annotation.Slave;
import com.ruoyi.common.security.annotation.RequiresPermissions;
import com.ruoyi.state.config.OrderProperties;
import com.ruoyi.state.domain.OrderTable;
import com.ruoyi.state.service.OrderTableService;
import com.ruoyi.system.api.RemoteBusinessService;
import com.ruoyi.system.api.RemoteUserService;
import com.ruoyi.system.api.domain.SysTest;
import com.ruoyi.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lsy
 * @description 测试
 * @date 2024/4/10
 */
@RestController
@RequestMapping("/test")
@RefreshScope
public class TestController extends BaseController {

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private RemoteBusinessService remoteBusinessService;

    @Autowired
    private OrderTableService orderTableService;

    @Value("${state.order.name}")
    private String name;

    @Autowired
    private OrderProperties orderProperties;

    @GetMapping("list")
    public String list(@RequestParam("username") String username) {
        R<List<SysTest>> list = remoteBusinessService.list(username, SecurityConstants.INNER);
        return list.toString();
    }

    @GetMapping("order/list")
    @Slave
    public TableDataInfo orderList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<OrderTable> page = orderTableService.page(new Page<>(pageNum, pageSize));
        System.out.println(name);
        System.out.println(orderProperties.getName());
        return getDataTable(page.getRecords());
    }

}
