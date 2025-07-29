package com.ruoyi.file.service;

import com.ruoyi.file.domain.SysFilePo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件上传接口
 * 
 * @author ruoyi
 */
public interface ISysFileService
{
    /**
     * 文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    public SysFilePo uploadFile(MultipartFile file) throws Exception;

    /**
     * 自定义存储位置文件上传
     *
     * @param file 上传文件
     * @param localPath 服务器存储路径
     * @return 访问地址
     */
    String uploadFile(MultipartFile file, String localPath) throws Exception;

    /**
     * 批量上传文件
     *
     * @param files 文件
     * @return 保存结果
     */
    List<SysFilePo> uploadFiles(MultipartFile[] files) throws Exception;
}
