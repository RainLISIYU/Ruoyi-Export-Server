package com.ruoyi.state.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 投票项设置
 * @TableName vote_option
 */
@TableName(value ="vote_option")
@Data
public class VoteOption implements Serializable {
    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 主题id
     */
    private Long subjectId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 图片路径
     */
    private String file;

    /**
     * 简介
     */
    private String summary;

    /**
     * 票数
     */
    private Integer voteNum;

    /**
     * 逻辑删除标识
     */
    private Integer deletedAt;

    /**
     * 用户名
     */
    @TableField(exist = false)
    private String username;

    /**
     * 文件路径
     */
    @TableField(exist = false)
    private String filePath;

    /**
     * 主题名称
     */
    @TableField(exist = false)
    private String subjectName;

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
        VoteOption other = (VoteOption) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getSubjectId() == null ? other.getSubjectId() == null : this.getSubjectId().equals(other.getSubjectId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getFile() == null ? other.getFile() == null : this.getFile().equals(other.getFile()))
            && (this.getSummary() == null ? other.getSummary() == null : this.getSummary().equals(other.getSummary()))
            && (this.getVoteNum() == null ? other.getVoteNum() == null : this.getVoteNum().equals(other.getVoteNum()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getSubjectId() == null) ? 0 : getSubjectId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getFile() == null) ? 0 : getFile().hashCode());
        result = prime * result + ((getSummary() == null) ? 0 : getSummary().hashCode());
        result = prime * result + ((getVoteNum() == null) ? 0 : getVoteNum().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", subjectId=").append(subjectId);
        sb.append(", userId=").append(userId);
        sb.append(", file=").append(file);
        sb.append(", summary=").append(summary);
        sb.append(", voteNum=").append(voteNum);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}