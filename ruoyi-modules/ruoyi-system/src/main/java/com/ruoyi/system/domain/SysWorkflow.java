package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 工作流配置表
 * @TableName sys_workflow
 */
@TableName(value ="sys_workflow")
@Data
public class SysWorkflow implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 工作流bpmn文件
     */
    private Long fileId;

    /**
     * 流程部署id
     */
    private String deploymentId;

    /**
     * 是否部署 0否 1是
     */
    private Integer needDeploy;

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

    /**
     * 删除标识
     */
    private Integer deletedAt;

    /**
     * 工作流图片id
     */
    private Long imgId;

    @TableField(exist = false)
    private String filePath;

    @TableField(exist = false)
    private String fileName;

    @TableField(exist = false)
    private String imgPath;

    @TableField(exist = false)
    private String imgName;

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
        SysWorkflow other = (SysWorkflow) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getFileId() == null ? other.getFileId() == null : this.getFileId().equals(other.getFileId()))
            && (this.getDeploymentId() == null ? other.getDeploymentId() == null : this.getDeploymentId().equals(other.getDeploymentId()))
            && (this.getNeedDeploy() == null ? other.getNeedDeploy() == null : this.getNeedDeploy().equals(other.getNeedDeploy())
            && (this.getCreatedAt() == null ? other.getCreatedAt() == null : this.getCreatedAt().equals(other.getCreatedAt()))
            && (this.getUpdatedAt() == null ? other.getUpdatedAt() == null : this.getUpdatedAt().equals(other.getUpdatedAt()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getImgId() == null ? other.getImgId() == null : this.getImgId().equals(other.getImgId())));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getFileId() == null) ? 0 : getFileId().hashCode());
        result = prime * result + ((getDeploymentId() == null) ? 0 : getDeploymentId().hashCode());
        result = prime * result + ((getNeedDeploy() == null) ? 0 : getNeedDeploy().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", fileId=").append(fileId);
        sb.append(", deploymentId=").append(deploymentId);
        sb.append(", needDeploy=").append(needDeploy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}