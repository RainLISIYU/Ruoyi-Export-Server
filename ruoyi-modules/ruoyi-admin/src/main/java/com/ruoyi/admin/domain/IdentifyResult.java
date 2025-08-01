package com.ruoyi.admin.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.common.core.annotation.Excel;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 检定数据表
 * @TableName business_identify_result
 */
@TableName(value ="business_identify_result")
@Data
public class IdentifyResult implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 序号
     */
    @Excel(name = "序号")
    private String no;

    /**
     * 证书编号
     */
    private String certificateNo;

    /**
     * 计量器具名称
     */
    @Excel(name = "计量器具名称")
    private String equipmentName;

    /**
     * 规格型号
     */
    @Excel(name = "型号规格")
    private String model;

    /**
     * 出厂编号
     */
    @Excel(name = "出厂编号")
    private String factoryNumber;

    /**
     * 样品编号
     */
    @Excel(name = "样品编号")
    private String sampleNumber;

    /**
     * 生产厂商
     */
    @Excel(name = "生产厂家")
    private String factoryName;

    /**
     * 准确度等级
     */
    @Excel(name = "准确度等级")
    private String accuracyClass;

    /**
     * 量程
     */
    @Excel(name = "量程(MPa)")
    private String scaleRange;

    /**
     * 分度值
     */
    @Excel(name = "分度值")
    private String resolution;

    /**
     * 备注/其他单位
     */
    @Excel(name = "备注/其他单位")
    private String remark;

    /**
     * 鉴定日期
     */
    private String appraisalDate;

    /**
     * 有效周期
     */
    private Integer effectivePeriod;

    /**
     * 鉴定结果
     */
    private String appraisalResult;

    /**
     * 计量标准id
     */
    private Long measuringStandard;

    /**
     * 检定地点
     */
    private String appraisalAddress;

    /**
     * 温度
     */
    private String temperature;

    /**
     * 湿度
     */
    private String humidity;

    /**
     * 删除标识,0-未删除,1-已删除
     */
    private Integer deletedAt;

    /**
     * 生成证书路径
     */
    private String certificateUrl;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private String createdAt;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updatedAt;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        IdentifyResult other = (IdentifyResult) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getNo() == null ? other.getNo() == null : this.getNo().equals(other.getNo()))
            && (this.getCertificateNo() == null ? other.getCertificateNo() == null : this.getCertificateNo().equals(other.getCertificateNo()))
            && (this.getEquipmentName() == null ? other.getEquipmentName() == null : this.getEquipmentName().equals(other.getEquipmentName()))
            && (this.getModel() == null ? other.getModel() == null : this.getModel().equals(other.getModel()))
            && (this.getFactoryNumber() == null ? other.getFactoryNumber() == null : this.getFactoryNumber().equals(other.getFactoryNumber()))
            && (this.getSampleNumber() == null ? other.getSampleNumber() == null : this.getSampleNumber().equals(other.getSampleNumber()))
            && (this.getFactoryName() == null ? other.getFactoryName() == null : this.getFactoryName().equals(other.getFactoryName()))
            && (this.getAccuracyClass() == null ? other.getAccuracyClass() == null : this.getAccuracyClass().equals(other.getAccuracyClass()))
            && (this.getScaleRange() == null ? other.getScaleRange() == null : this.getScaleRange().equals(other.getScaleRange()))
            && (this.getResolution() == null ? other.getResolution() == null : this.getResolution().equals(other.getResolution()))
            && (this.getAppraisalDate() == null ? other.getAppraisalDate() == null : this.getAppraisalDate().equals(other.getAppraisalDate()))
            && (this.getEffectivePeriod() == null ? other.getEffectivePeriod() == null : this.getEffectivePeriod().equals(other.getEffectivePeriod()))
            && (this.getAppraisalResult() == null ? other.getAppraisalResult() == null : this.getAppraisalResult().equals(other.getAppraisalResult()))
            && (this.getMeasuringStandard() == null ? other.getMeasuringStandard() == null : this.getMeasuringStandard().equals(other.getMeasuringStandard()))
            && (this.getAppraisalAddress() == null ? other.getAppraisalAddress() == null : this.getAppraisalAddress().equals(other.getAppraisalAddress()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()))
            && (this.getHumidity() == null ? other.getHumidity() == null : this.getHumidity().equals(other.getHumidity()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getNo() == null) ? 0 : getNo().hashCode());
        result = prime * result + ((getCertificateNo() == null) ? 0 : getCertificateNo().hashCode());
        result = prime * result + ((getEquipmentName() == null) ? 0 : getEquipmentName().hashCode());
        result = prime * result + ((getModel() == null) ? 0 : getModel().hashCode());
        result = prime * result + ((getFactoryNumber() == null) ? 0 : getFactoryNumber().hashCode());
        result = prime * result + ((getSampleNumber() == null) ? 0 : getSampleNumber().hashCode());
        result = prime * result + ((getFactoryName() == null) ? 0 : getFactoryName().hashCode());
        result = prime * result + ((getAccuracyClass() == null) ? 0 : getAccuracyClass().hashCode());
        result = prime * result + ((getScaleRange() == null) ? 0 : getScaleRange().hashCode());
        result = prime * result + ((getResolution() == null) ? 0 : getResolution().hashCode());
        result = prime * result + ((getAppraisalDate() == null) ? 0 : getAppraisalDate().hashCode());
        result = prime * result + ((getEffectivePeriod() == null) ? 0 : getEffectivePeriod().hashCode());
        result = prime * result + ((getAppraisalResult() == null) ? 0 : getAppraisalResult().hashCode());
        result = prime * result + ((getMeasuringStandard() == null) ? 0 : getMeasuringStandard().hashCode());
        result = prime * result + ((getAppraisalAddress() == null) ? 0 : getAppraisalAddress().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        result = prime * result + ((getHumidity() == null) ? 0 : getHumidity().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getCreatedAt() == null) ? 0 : getCreatedAt().hashCode());
        result = prime * result + ((getUpdatedAt() == null) ? 0 : getUpdatedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", no=").append(no);
        sb.append(", certificateNo=").append(certificateNo);
        sb.append(", equipmentName=").append(equipmentName);
        sb.append(", model=").append(model);
        sb.append(", factoryNumber=").append(factoryNumber);
        sb.append(", sampleNumber=").append(sampleNumber);
        sb.append(", factoryName=").append(factoryName);
        sb.append(", accuracyClass=").append(accuracyClass);
        sb.append(", range=").append(scaleRange);
        sb.append(", resolution=").append(resolution);
        sb.append(", appraisalDate=").append(appraisalDate);
        sb.append(", effectivePeriod=").append(effectivePeriod);
        sb.append(", appraisalResult=").append(appraisalResult);
        sb.append(", measuringStandard=").append(measuringStandard);
        sb.append(", appraisalAddress=").append(appraisalAddress);
        sb.append(", temperature=").append(temperature);
        sb.append(", humidity=").append(humidity);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", createdAt=").append(createdAt);
        sb.append(", updatedAt=").append(updatedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}