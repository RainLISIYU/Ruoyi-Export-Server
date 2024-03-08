package com.ruoyi.business.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

/**
 * 测试表(MyTest)表实体类
 *
 * @author chenqiang
 * @since 2024-03-08 14:08:05
 */
@SuppressWarnings("serial")
public class MyTest extends Model<MyTest> {
    //主键
    private Long id;
    //名称
    private String name;
    //地址
    private String address;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}

