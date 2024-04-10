package com.ruoyi.state.domain;/*
 *@Author:cq
 *@Date:2024/4/10 14:39
 */

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lsy
 * @description 订单实体
 * @date 2024/4/10
 */
@Data
@TableName("order_table")
public class OrderTable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单名称
     */
    private String orderName;

    /**
     * 交易时间
     */
    private LocalDateTime tradeTime;

}
