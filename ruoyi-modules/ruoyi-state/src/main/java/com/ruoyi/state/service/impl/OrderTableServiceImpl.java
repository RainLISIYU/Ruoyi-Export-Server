package com.ruoyi.state.service.impl;/*
 *@Author:cq
 *@Date:2024/4/10 14:48
 */

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.state.domain.OrderTable;
import com.ruoyi.state.mapper.OrderTableMapper;
import com.ruoyi.state.service.OrderTableService;
import org.springframework.stereotype.Service;

/**
 * @author lsy
 * @description 订单service
 * @date 2024/4/10
 */
@Service("orderTableService")
public class OrderTableServiceImpl extends ServiceImpl<OrderTableMapper, OrderTable> implements OrderTableService {
}
