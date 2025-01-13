package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysFile;
import com.ruoyi.system.api.factory.RemoteFileFallbackFactory;

import java.util.Collection;
import java.util.List;

/**
 * 文件服务
 * 
 * @author ruoyi
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface  RemoteFileService
{
    /**
     * 上传文件
     *
     * @param file 文件信息
     * @return 结果
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<SysFile> upload(@RequestPart(value = "file") MultipartFile file);

    /**
     * 根据文件id查询文件信息
     *
     * @param fileIds 文件id集合
     * @return 结果集
     */
    @GetMapping("/listFiles")
    public R<List<SysFile>> listFiles(@RequestParam("fileIds") Collection<Long> fileIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据md5查询文件信息
     *
     * @param md5 加密数据
     * @param source 头部认证
     * @return 文件信息
     */
    @GetMapping("listByMd5")
    public R<List<SysFile>> listByMd5(@RequestParam("md5") String md5, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
