package com.ruoyi.file.service.impl;

import java.io.InputStream;
import java.util.List;

import com.ruoyi.file.domain.SysFilePo;
import com.ruoyi.file.mapper.SysFileMapper;
import com.ruoyi.file.service.ISysFileService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.nacos.common.utils.IoUtils;
import com.ruoyi.file.config.MinioConfig;
import com.ruoyi.file.utils.FileUploadUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

/**
 * Minio 文件存储
 *
 * @author ruoyi
 */
@Service
public class MinioSysFileServiceImpl implements ISysFileService
{

    @Resource
    private SysFileMapper sysFileMapper;

    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * Minio文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public SysFilePo uploadFile(MultipartFile file) throws Exception
    {
        String fileName = FileUploadUtils.extractFilename(file);
        InputStream inputStream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(inputStream, file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        IoUtils.closeQuietly(inputStream);
        // 保存
        SysFilePo sysFilePo = new SysFilePo();
        sysFilePo.setRemotePath(minioConfig.getUrl() + "/" + minioConfig.getBucketName() + "/" + fileName);
        sysFilePo.setFileName(file.getOriginalFilename());
        sysFilePo.setFileSize(String.valueOf(file.getSize() / 1024));
        sysFileMapper.insert(sysFilePo);
        return sysFilePo;
    }

    @Override
    public String uploadFile(MultipartFile file, String localPath) throws Exception {
        return null;
    }

    @Override
    public List<SysFilePo> uploadFiles(MultipartFile[] files) {
        return null;
    }
}
