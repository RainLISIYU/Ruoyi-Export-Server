package com.ruoyi.file.service.impl;

import com.ruoyi.file.domain.SysFilePo;
import com.ruoyi.file.service.ISysFileService;
import com.ruoyi.file.service.SysFileService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.file.utils.FileUploadUtils;

import java.time.LocalDateTime;

/**
 * 本地文件存储
 * 
 * @author ruoyi
 */
@Primary
@Service
public class LocalSysFileServiceImpl implements ISysFileService
{

    @Resource
    private SysFileService sysFileService;

    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain}")
    public String domain;
    
    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 本地文件上传接口
     * 
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception e
     */
    @Override
    public SysFilePo uploadFile(MultipartFile file) throws Exception
    {
        // 上传文件并返回相对路径
        String name = FileUploadUtils.upload(localFilePath, file);
        // 服务器本地路径
        String localPath = localFilePath + name;
        // 上传文件大小
        long fileSize = file.getSize() / 1024;
        // 远程路径
        String remotePath = domain + localFilePrefix + name;
        // 保存文件信息
        SysFilePo sysFilePo = new SysFilePo();
        sysFilePo.setLocalPath(localPath);
        sysFilePo.setRemotePath(remotePath);
        sysFilePo.setFileName(file.getOriginalFilename());
        sysFilePo.setFileSize(String.valueOf(fileSize));
        sysFilePo.setUploadTime(LocalDateTime.now());
        sysFileService.save(sysFilePo);
        // 返回
        return sysFilePo;
    }

    /**
     * 自定义路径上传
     *
     * @param file 上传文件
     * @param localPath 服务器存储路径
     * @return 访问地址
     * @throws Exception e
     */
    @Override
    public String uploadFile(MultipartFile file, String localPath) throws Exception {
        String name = FileUploadUtils.upload(localPath, file);
        return domain + localFilePrefix + name;
    }

}
