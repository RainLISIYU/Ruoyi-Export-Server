package com.ruoyi.business.domain;

import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.core.annotation.Excel;
import com.ruoyi.common.core.web.domain.BaseEntity;

import java.io.Serial;

/**
 * 测试对象 my_test
 * 
 * @author ruoyi
 * @date 2024-03-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyTest extends BaseEntity
{
    @Serial
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    @Excel(name = "删除标识")
    private Integer deletedAt;

    @Version
    private Integer version;

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("address", getAddress())
            .toString();
    }
}
