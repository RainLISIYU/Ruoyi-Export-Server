package com.ruoyi.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 工作流名称
     */
    private String name;

    /**
     * 工作流bpmn文件
     */
    private String file;

    /**
     * 流程部署id
     */
    private String deploymentId;

    /**
     * 是否部署 0否 1是
     */
    private Integer needDeploy;

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
            && (this.getFile() == null ? other.getFile() == null : this.getFile().equals(other.getFile()))
            && (this.getDeploymentId() == null ? other.getDeploymentId() == null : this.getDeploymentId().equals(other.getDeploymentId()))
            && (this.getNeedDeploy() == null ? other.getNeedDeploy() == null : this.getNeedDeploy().equals(other.getNeedDeploy()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getFile() == null) ? 0 : getFile().hashCode());
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
        sb.append(", file=").append(file);
        sb.append(", deploymentId=").append(deploymentId);
        sb.append(", needDeploy=").append(needDeploy);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}