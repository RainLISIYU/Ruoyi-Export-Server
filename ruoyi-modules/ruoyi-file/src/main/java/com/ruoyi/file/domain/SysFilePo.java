package com.ruoyi.file.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 文件记录表
 * @TableName sys_file
 */
@TableName(value ="sys_file")
public class SysFilePo implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小
     */
    private String fileSize;

    /**
     * 远程地址
     */
    private String remotePath;

    /**
     * 本地地址
     */
    private String localPath;

    /**
     * 删除标识 0 否 1 是
     */
    private Boolean deletedAt;

    /**
     * 上传日期
     */
    private LocalDateTime uploadTime;

    /**
     * md5
     */
    private String md5;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 文件名称
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 文件名称
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 文件大小
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * 文件大小
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * 远程地址
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * 远程地址
     */
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    /**
     * 本地地址
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * 本地地址
     */
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /**
     * 删除标识 0 否 1 是
     */
    public Boolean getDeletedAt() {
        return deletedAt;
    }

    /**
     * 删除标识 0 否 1 是
     */
    public void setDeletedAt(Boolean deletedAt) {
        this.deletedAt = deletedAt;
    }

    /**
     * 上传日期
     */
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    /**
     * 上传日期
     */
    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    /**
     * 获取MD5
     */
    public String getMd5() {
        return md5;
    }

    /**
     * 设置MD5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }

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
        SysFilePo other = (SysFilePo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileName() == null ? other.getFileName() == null : this.getFileName().equals(other.getFileName()))
            && (this.getFileSize() == null ? other.getFileSize() == null : this.getFileSize().equals(other.getFileSize()))
            && (this.getRemotePath() == null ? other.getRemotePath() == null : this.getRemotePath().equals(other.getRemotePath()))
            && (this.getLocalPath() == null ? other.getLocalPath() == null : this.getLocalPath().equals(other.getLocalPath()))
            && (this.getDeletedAt() == null ? other.getDeletedAt() == null : this.getDeletedAt().equals(other.getDeletedAt()))
            && (this.getUploadTime() == null ? other.getUploadTime() == null : this.getUploadTime().equals(other.getUploadTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileName() == null) ? 0 : getFileName().hashCode());
        result = prime * result + ((getFileSize() == null) ? 0 : getFileSize().hashCode());
        result = prime * result + ((getRemotePath() == null) ? 0 : getRemotePath().hashCode());
        result = prime * result + ((getLocalPath() == null) ? 0 : getLocalPath().hashCode());
        result = prime * result + ((getDeletedAt() == null) ? 0 : getDeletedAt().hashCode());
        result = prime * result + ((getUploadTime() == null) ? 0 : getUploadTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", fileName=").append(fileName);
        sb.append(", fileSize=").append(fileSize);
        sb.append(", remotePath=").append(remotePath);
        sb.append(", localPath=").append(localPath);
        sb.append(", deletedAt=").append(deletedAt);
        sb.append(", uploadTime=").append(uploadTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}