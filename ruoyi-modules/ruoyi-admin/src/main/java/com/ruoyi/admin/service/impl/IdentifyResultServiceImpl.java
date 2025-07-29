package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.admin.domain.IdentifyResult;
import com.ruoyi.admin.service.IdentifyResultService;
import com.ruoyi.admin.mapper.IdentifyResultMapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.security.utils.DictUtils;
import com.ruoyi.system.api.RemoteDictService;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.domain.SysFile;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
public class IdentifyResultServiceImpl extends ServiceImpl<IdentifyResultMapper, IdentifyResult>
    implements IdentifyResultService{

    @Resource
    private RemoteFileService remoteFileService;

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private final String ORDER_MAX_NO = "certified_max_no:";

    private Logger logger = LoggerFactory.getLogger(IdentifyResultServiceImpl.class);

    @Override
    public Page<IdentifyResult> getPage(IdentifyResult identifyResult, Integer pi, Integer ps) {
        LambdaQueryWrapper<IdentifyResult> queryWrapper = new LambdaQueryWrapper<>();
        Page<IdentifyResult> page = this.page(new Page<>(pi, ps), queryWrapper);
        return page;
    }

    @Override
    public AjaxResult importData(MultipartFile[] file) {
        ExcelUtil<IdentifyResult> util = new ExcelUtil<>(IdentifyResult.class);
        logger.info("开始导入数据{}", MDC.get(SecurityConstants.TRACE_ID));
        // 保存导入文件
//        R<SysFile> upload = remoteFileService.upload(file);
        R<List<SysFile>> uploadResult = remoteFileService.uploadFiles(file);
        if (!R.isSuccess(uploadResult)) {
            return AjaxResult.error("文件保存失败");
        }
        // 获取导入字典参数
        R<List<SysDictData>> sysExportOption = remoteDictService.feignDictType("sys_export_option", SecurityConstants.INNER);
        if (!R.isSuccess(sysExportOption)) {
            return AjaxResult.error("导入参数获取失败");
        }
        // 获取字典map
        Map<String, String> dictDataMap = DictUtils.dictDataToMap(sysExportOption.getData());

        boolean startFlag = true;
        for (MultipartFile upFile : file) {
            // 分批导入数据
            try(InputStream is = upFile.getInputStream()) {
                List<IdentifyResult> list = new ArrayList<>();
                do {
                    list = util.importExcel(is, 3, Integer.valueOf(dictDataMap.get("batch_size")), startFlag);
                    startFlag = false;

                    logger.info("导入数据:{}", list.size());
                } while (! list.isEmpty());
            } catch (Exception e) {
                logger.error("导入失败:{}", e.getMessage());
                e.printStackTrace();
            }
        }
        return AjaxResult.success();
    }

    private String getCertificateNo(Map<String, String> dataMap) {
        // 获取前缀字母
        String prefix = dataMap.get("certificate_prefix");
        // 当前年
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        prefix = prefix + year.substring(year.length() - 2);
        // 缓存key
        String cacheKey = ORDER_MAX_NO + prefix;
        // 查询当年最大编号数据
        List<IdentifyResult> maxNoData = this.list(new QueryWrapper<IdentifyResult>().select("MAX(certificate_no) AS certificateNo").lambda().likeLeft(IdentifyResult::getCertificateNo, prefix));
        // 查询缓存中的最大编号
        Long maxNo = redisTemplate.opsForValue().increment(cacheKey);
        synchronized (this) {
            if (StringUtils.isNull(maxNo) && (StringUtils.isNull(maxNoData) || maxNoData.isEmpty())) {
                // 数据库中没数据，则从配置中获取，配置中没有从0开始
                maxNo = StringUtils.isEmpty(dataMap.get("certificate_no")) ? 0L : Long.parseLong(dataMap.get("certificate_no"));
                // 设置缓存
                redisTemplate.opsForValue().set(cacheKey, maxNo, 5, TimeUnit.SECONDS);
                return prefix + String.format("%04d", maxNo);
            }
        }
        if (! StringUtils.isNull(maxNo)) {
            // 缓存中有编号，直接设置
            return prefix + String.format("%04d", maxNo);
        }
        // 存在则最大编号后四位+1
        IdentifyResult maxData = maxNoData.getFirst();
        String certificateNo = maxData.getCertificateNo();
        maxNo = Long.parseLong(certificateNo.substring(certificateNo.length() - 4)) + 1;
        // 设置缓存
        redisTemplate.opsForValue().set(cacheKey, maxNo);
        return prefix + String.format("%04d", maxNo);
    }

}




