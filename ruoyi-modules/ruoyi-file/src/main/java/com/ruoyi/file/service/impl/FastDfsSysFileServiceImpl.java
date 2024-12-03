package com.ruoyi.file.service.impl;

import java.io.InputStream;
import com.alibaba.nacos.common.utils.IoUtils;
import com.ruoyi.file.domain.SysFilePo;
import com.ruoyi.file.mapper.SysFileMapper;
import com.ruoyi.file.service.ISysFileService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.ruoyi.common.core.utils.file.FileTypeUtils;

/**
 * FastDFS 文件存储
 *
 * @author ruoyi
 */
@Service
public class FastDfsSysFileServiceImpl implements ISysFileService
{
    @Resource
    private SysFileMapper sysFileMapper;

    /**
     * 域名或本机访问地址
     */
    @Value("${fdfs.domain}")
    public String domain;

    @Autowired(required = false)
    private FastFileStorageClient storageClient;

    /**
     * FastDfs文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public SysFilePo uploadFile(MultipartFile file) throws Exception
    {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = storageClient.uploadFile(inputStream, file.getSize(),
                FileTypeUtils.getExtension(file), null);
        IoUtils.closeQuietly(inputStream);
        SysFilePo sysFilePo = new SysFilePo();
        sysFilePo.setRemotePath(domain + "/" + storePath.getFullPath());
        sysFilePo.setFileSize(String.valueOf(file.getSize() / 1024));
        sysFilePo.setFileName(file.getOriginalFilename());
        sysFileMapper.insert(sysFilePo);
        return sysFilePo;
    }

    @Override
    public String uploadFile(MultipartFile file, String localPath) throws Exception {
        return null;
    }
}
