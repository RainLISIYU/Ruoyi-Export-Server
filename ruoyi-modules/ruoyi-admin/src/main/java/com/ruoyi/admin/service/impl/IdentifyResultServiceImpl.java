package com.ruoyi.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.data.Texts;
import com.ruoyi.admin.domain.CertificateTemplate;
import com.ruoyi.admin.domain.IdentifyResult;
import com.ruoyi.admin.service.CertificateTemplateService;
import com.ruoyi.admin.service.IdentifyResultService;
import com.ruoyi.admin.mapper.IdentifyResultMapper;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.utils.DateUtils;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.file.FileUtils;
import com.ruoyi.common.core.utils.poi.ExcelUtil;
import com.ruoyi.common.core.utils.poi.WordUtil;
import com.ruoyi.common.core.web.domain.AjaxResult;
import com.ruoyi.common.datasource.config.MyMetaObjectHandler;
import com.ruoyi.common.security.utils.DictUtils;
import com.ruoyi.system.api.RemoteDictService;
import com.ruoyi.system.api.RemoteFileService;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.domain.SysFile;
import jakarta.annotation.Resource;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
@Service
public class IdentifyResultServiceImpl extends ServiceImpl<IdentifyResultMapper, IdentifyResult>
    implements IdentifyResultService{

    @Resource
    private CertificateTemplateService certificateTemplateService;

    @Resource
    private RemoteFileService remoteFileService;

    @Resource
    private RemoteDictService remoteDictService;

    @Resource
    private MyMetaObjectHandler myMetaObjectHandler;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${file.path}")
    private String path;

    @Value("${file.prefix}")
    private String prefix;

    private final String ORDER_MAX_NO = "certified_max_no:";

    @Resource
    private Redisson redisson;

    private Logger logger = LoggerFactory.getLogger(IdentifyResultServiceImpl.class);

    @Override
    public Page<IdentifyResult> getPage(IdentifyResult identifyResult, Integer pi, Integer ps) {
        LambdaQueryWrapper<IdentifyResult> queryWrapper = new LambdaQueryWrapper<>();
        Page<IdentifyResult> page = this.page(new Page<>(pi, ps), queryWrapper);
        return page;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
                List<IdentifyResult> list;
                do {
                    list = util.importExcel(is, 3, dictDataMap.get("batch_size") == null ? 100 : Integer.parseInt(dictDataMap.get("batch_size")), startFlag);
                    startFlag = false;
                    // 处理导入数据
                    handleImportData(list, getCertificateNos(dictDataMap, list.size()));
                    logger.info("导入数据:{}", list.size());
                    // 保存数据
                    this.saveBatch(list);
                } while (! list.isEmpty());
            } catch (Exception e) {
                logger.error("导入失败:{}", e.getMessage());
                // 删除编号缓存
                String cacheKey = ORDER_MAX_NO + getCachePrefix(dictDataMap);
                redisTemplate.delete(cacheKey);
            }
        }
        return AjaxResult.success();
    }

    @Override
    public AjaxResult uploadTemplate(MultipartFile file) {
        // 文件上传
        R<SysFile> upload = remoteFileService.upload(file);
        if (!R.isSuccess(upload)) {
            return AjaxResult.error(upload.getMsg());
        }
        // 保存模板文件信息
        SysFile uploadFile = upload.getData();
        CertificateTemplate template = new CertificateTemplate();
        template.setFileId(uploadFile.getId());
        template.setFileUrl(uploadFile.getUrl());
        template.setFileName(FileUtils.getName(uploadFile.getUrl()));
        certificateTemplateService.save(template);
        return AjaxResult.success(upload);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult genCertificate(List<String> ids) {
        // 查询需要生成的数据
        List<IdentifyResult> identifyResults = this.listByIds(ids);
        // 查询最新模板信息
        CertificateTemplate certificateTemplate = certificateTemplateService.getLastTemplate();
        if (StringUtils.isNull(certificateTemplate)) {
            return AjaxResult.error("请先上传模板");
        }
        // 获取导入字典参数
        R<List<SysDictData>> sysExportOption = remoteDictService.feignDictType("sys_export_option", SecurityConstants.INNER);
        if (!R.isSuccess(sysExportOption)) {
            return AjaxResult.error("导入参数获取失败");
        }
        // 获取字典map
        Map<String, String> dictDataMap = DictUtils.dictDataToMap(sysExportOption.getData());
        // 根据数据生成文件
        boolean result = resultToCertificate(identifyResults, certificateTemplate, dictDataMap);
        if (!result) {
            return AjaxResult.error("证书生成失败");
        }
        // 保存证书信息
        this.updateBatchById(identifyResults, dictDataMap.get("batch_size") == null ? 500 : Integer.parseInt(dictDataMap.get("batch_size")));
        return AjaxResult.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult delCertificate(List<String> ids) {
        boolean res = this.removeBatchByIds(ids);
        if (! res) {
            return AjaxResult.error("删除失败");
        }
        // 获取导入字典参数
        R<List<SysDictData>> sysExportOption = remoteDictService.feignDictType("sys_export_option", SecurityConstants.INNER);
        if (!R.isSuccess(sysExportOption)) {
            return AjaxResult.error("导入参数获取失败");
        }
        // 获取字典map
        Map<String, String> dictDataMap = DictUtils.dictDataToMap(sysExportOption.getData());
        redisTemplate.delete(ORDER_MAX_NO + getCachePrefix(dictDataMap));
        // 删除编号缓存
        String cacheKey = ORDER_MAX_NO + getCachePrefix(dictDataMap);
        redisTemplate.delete(cacheKey);
        return AjaxResult.success();
    }

    @Override
    public IdentifyResult getIdentifyResultById(String id) {
        return this.getById(id);
    }

    /**
     * 根据检定结果生成证书
     *
     * @param identifyResults 检定结果
     * @param certificateTemplate 模板信息
     * @return 是否成功
     */
    private boolean resultToCertificate(List<IdentifyResult> identifyResults, CertificateTemplate certificateTemplate, Map<String, String> dictDataMap) {
        for (IdentifyResult identifyResult : identifyResults) {
            // 模板map
            Map<String, Object> dataMap = new HashMap<>();
            // 生成文件路径
            String fileName = extractFilename(identifyResult, dictDataMap);
            // 获取模板路径
            String remoteUrl = certificateTemplate.getFileUrl();
            String localUrl = remoteUrl.replace(prefix, path);
            try {
                // 设置参数
                resultToMap(identifyResult, dataMap);
                // 生成文件
                WordUtil.exportWordByTemplate(localUrl, path + fileName, dataMap);
            } catch (Exception e) {
                logger.error("生成证书失败:{}", e.getMessage());
                e.printStackTrace();
                return false;
            }
            identifyResult.setCertificateUrl(prefix + fileName);
        }
        return true;
    }

    /**
     * 根据导入数据生成map
     * @param identifyResult 导入数据
     * @param dataMap 模板map
     */
    private void resultToMap(IdentifyResult identifyResult, Map<String, Object> dataMap) throws ParseException {
        dataMap.put("certificateNo", identifyResult.getCertificateNo());
        dataMap.put("certificateNo1", identifyResult.getCertificateNo());
        dataMap.put("submissionUnit", Texts.of("1").create());
        dataMap.put("equipmentName", Texts.of(identifyResult.getEquipmentName()).create());
        dataMap.put("model", Texts.of(identifyResult.getModel()).create());
        dataMap.put("factoryNumber", Texts.of(identifyResult.getFactoryNumber()).create());
        dataMap.put("factoryName", Texts.of(identifyResult.getFactoryName()).create());
        dataMap.put("basisCode", Texts.of("1").create());
        dataMap.put("verifyResult", Texts.of("1").create());
        if (StringUtils.isNotEmpty(identifyResult.getApproveUrl())) {
            dataMap.put("approve", Pictures.of(identifyResult.getApproveUrl().replace(prefix, path)).size(86, 35).create());
        }
        if (StringUtils.isNotEmpty(identifyResult.getCheckUrl())) {
            dataMap.put("verify1", Pictures.of(identifyResult.getCheckUrl().replace(prefix, path)).size(86, 35).create());
        }
        if (StringUtils.isNotEmpty(identifyResult.getVerifyUrl())) {
            dataMap.put("verify2", Pictures.of(identifyResult.getVerifyUrl().replace(prefix, path)).size(86, 35).create());
        }
        String verifyDate = identifyResult.getAppraisalDate();
        if (StringUtils.isNotEmpty(verifyDate)) {
            if (verifyDate.length() > 10) {
                verifyDate = verifyDate.substring(0, 10);
            }
            LocalDate verifyLocalDate = LocalDate.parse(verifyDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate periodLocalDate = verifyLocalDate.plusMonths(identifyResult.getEffectivePeriod()).plusDays(-1);
            String periodDate = periodLocalDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            dataMap.put("y1", verifyDate.split("-")[0]);
            dataMap.put("m1", verifyDate.split("-")[1]);
            dataMap.put("d1", verifyDate.split("-")[2]);
            dataMap.put("y2", periodDate.split("-")[0]);
            dataMap.put("m2", periodDate.split("-")[1]);
            dataMap.put("d2", periodDate.split("-")[2]);
        }
        dataMap.put("standardName", Texts.of("1").create());
        dataMap.put("standardRange", Texts.of("1").create());
        dataMap.put("standardNo", Texts.of("1").create());
        dataMap.put("standardPeriod", Texts.of("1").create());
        dataMap.put("appraisalAddress", Texts.of("1").create());
        dataMap.put("temperature", Texts.of("1").create());
        dataMap.put("humidity", Texts.of("1").create());
        dataMap.put("requirement1", Texts.of("1").create());
        dataMap.put("data1", Texts.of("1").create());
        dataMap.put("res1", Texts.of("1").create());
        dataMap.put("requirement2", Texts.of("1").create());
        dataMap.put("data2", Texts.of("1").create());
        dataMap.put("res2", Texts.of("1").create());
        dataMap.put("requirement3", Texts.of("1").create());
        dataMap.put("data3", Texts.of("1").create());
        dataMap.put("res3", Texts.of("1").create());
        dataMap.put("requirement4", Texts.of("1").create());
        dataMap.put("data4", Texts.of("1").create());
        dataMap.put("res4", Texts.of("1").create());
        dataMap.put("requirement5", Texts.of("1").create());
        dataMap.put("data5", Texts.of("1").create());
        dataMap.put("res5", Texts.of("1").create());
        dataMap.put("requirement6", Texts.of("1").create());
        dataMap.put("data6", Texts.of("1").create());
        dataMap.put("res6", Texts.of("1").create());
    }

    private String extractFilename(IdentifyResult identifyResult, Map<String, String> dictDataMap) {
        // 获取配置中的文件名
        String fileName = dictDataMap.get("certificate_name") == null ? "一般压力表检定证书" : dictDataMap.get("certificate_name");
        // 获取当前证书编号后4位
        String certificateNo = identifyResult.getCertificateNo();
        certificateNo = certificateNo.substring(certificateNo.length() - 4);
        // 文件类型
        String fileType = dictDataMap.get("file_type") == null ? "docx" : dictDataMap.get("file_type");
        return StringUtils.format("/{}/certificate/{}{}.{}", DateUtils.datePath(), fileName, certificateNo, fileType);
    }

    /**
     * 处理导入数据
     *
     * @param dataList 导入数据
     * @param certificateNos 证书编号
     */
    private void handleImportData(List<IdentifyResult> dataList, List<String> certificateNos) {
        for (int i = 0; i < dataList.size(); i++) {
            dataList.get(i).setCertificateNo(certificateNos.get(i));
        }
    }

    /**
     * 批量生产证书编号
     *
     * @param dataMap 字典数据
     * @param size 数据量
     * @return 证书编号
     */
    private synchronized List<String> getCertificateNos(Map<String, String> dataMap, int size) {
        // 编号列表
        List<String> certificateNos = new ArrayList<>();
        // 获取前缀
        String prefix = getCachePrefix(dataMap);
        // 获取当前最大编号
        Long maxNo = getCertificateNo(dataMap);
        logger.info("开始编号:{}", maxNo);
        // 生成编号
        for (int i = 0; i < size; i++) {
            certificateNos.add(prefix + String.format("%4d", maxNo++));
        }
        logger.info("结束编号:{}", maxNo-1);
        // 设置缓存编号
        Long increment = redisTemplate.opsForValue().increment(ORDER_MAX_NO + prefix, size - 1);
        logger.info("缓存编号：{}", increment);
        return certificateNos;
    }

    /**
     * 编号前缀 配置项+年
     * @param dataMap 字典配置数据
     * @return 前缀
     */
    private String getCachePrefix(Map<String, String> dataMap) {
        // 获取前缀字母
        String prefix = dataMap.get("certificate_prefix");
        if (StringUtils.isEmpty( prefix )) {
            prefix = "HDYB";
        }
        // 当前年
        String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
        prefix = prefix + year;
        return prefix;
    }

    /**
     * 生成自增唯一编号，缓存中有拿缓存，没有查询数据库最大值，没有拿配置，有最大值+1
     *
     * @param dataMap 字典数据
     * @return 证书编号
     */
    private Long getCertificateNo(Map<String, String> dataMap) {
        // 获取缓存key
        String prefix = getCachePrefix(dataMap);
        String cacheKey = ORDER_MAX_NO + prefix;
        // 查询缓存中的最大编号
        Long maxNo;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            // 缓存中有编号，直接设置
            maxNo = redisTemplate.opsForValue().increment(cacheKey);
            return maxNo;
        }
        // 查询缓存中的最大编号
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey))) {
            // 缓存中有编号，直接设置
            maxNo = redisTemplate.opsForValue().increment(cacheKey);
            return maxNo;
        }
        // 查询当年最大编号数据
        List<IdentifyResult> maxNoData = this.list(new QueryWrapper<IdentifyResult>().select("MAX(certificate_no) AS certificateNo").lambda().likeRight(IdentifyResult::getCertificateNo, prefix));
        if (StringUtils.isNull(maxNoData) || maxNoData.isEmpty() || StringUtils.isNull(maxNoData.getFirst())) {
            // 数据库中没数据，则从配置中获取，配置中没有从0开始
            maxNo = StringUtils.isEmpty(dataMap.get("certificate_no")) ? 0L : Long.parseLong(dataMap.get("certificate_no"));
            // 设置缓存
            maxNo = redisTemplate.opsForValue().increment(cacheKey, maxNo);
            return maxNo;
        }
        // 存在则最大编号后四位+1
        IdentifyResult maxData = maxNoData.getFirst();
        String certificateNo = maxData.getCertificateNo();
        maxNo = Long.parseLong(certificateNo.substring(certificateNo.length() - 4));
        // 设置缓存
        maxNo = redisTemplate.opsForValue().increment(cacheKey, maxNo - 1);
        return maxNo;
    }

}




