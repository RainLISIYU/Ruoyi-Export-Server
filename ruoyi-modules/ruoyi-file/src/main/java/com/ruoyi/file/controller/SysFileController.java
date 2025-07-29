package com.ruoyi.file.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.file.domain.SysFilePo;
import com.ruoyi.file.service.SysFileService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.ruoyi.file.service.ISysFileService;
import com.ruoyi.system.api.domain.SysFile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 文件请求处理
 * 
 * @author ruoyi
 */
@RestController
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Resource
    private ISysFileService sysFileService;

    @Resource
    private SysFileService queryService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(@RequestParam("file") MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            SysFilePo sysFilePo = sysFileService.uploadFile(file);
            String url = sysFilePo.getRemotePath();
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            sysFile.setId(sysFilePo.getId());
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    @PostMapping("uploadFiles")
    public R<List<SysFile>> uploadFiles(@RequestParam("file") MultipartFile[] file) {
        log.info("上传文件开始");
        List<SysFilePo> files;
        try {
            // 文件上传保存
            files = sysFileService.uploadFiles(file);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
        // 处理返回结果
        List<SysFile> sysFiles = new ArrayList<>();
        if (files.isEmpty()) {
            return R.ok(sysFiles);
        }
        files.forEach(sysFilePo -> {
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(sysFilePo.getRemotePath()));
            sysFile.setUrl(sysFilePo.getRemotePath());
            sysFile.setId(sysFilePo.getId());
            sysFiles.add(sysFile);
        });
        return R.ok(sysFiles);
    }

    /**
     * 根据文件id查询文件信息
     *
     * @param fileIds 文件id
     * @return 文件列表
     */
    @GetMapping("listFiles")
    public R<List<SysFile>> listFiles(@RequestParam("fileIds") Set<Long> fileIds){
        List<SysFilePo> sysFilePos = queryService.listByIds(fileIds);
        List<SysFile> sysFiles = sysFilePos.stream().map(sysFilePo -> {
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(sysFilePo.getRemotePath()));
            sysFile.setUrl(sysFilePo.getRemotePath());
            sysFile.setId(sysFilePo.getId());
            return sysFile;
        }).toList();
        return R.ok(sysFiles);
    }

    /**
     * 根据MD5获取文件信息
     *
     * @param md5 文件MD5
     * @return 文件列表
     */
    @GetMapping("listByMd5")
    public R<List<SysFile>> listByMd5(@RequestParam("md5") String md5){
        LambdaQueryWrapper<SysFilePo> queryWrapper = new LambdaQueryWrapper<SysFilePo>().eq(SysFilePo::getMd5, md5);
        List<SysFilePo> sysFilePos = queryService.list(queryWrapper);
        List<SysFile> sysFiles = new ArrayList<>();
        for (SysFilePo file: sysFilePos) {
            SysFile sysFile = new SysFile();
            sysFile.setName(file.getFileName());
            sysFile.setUrl(file.getRemotePath());
            sysFile.setId(file.getId());
            sysFiles.add(sysFile);
        }
        return R.ok(sysFiles);
    }

}