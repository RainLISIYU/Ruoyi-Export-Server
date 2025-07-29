package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysDictData;
import com.ruoyi.system.api.factory.RemoteDictFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程调用字典接口
 */
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteDictFallbackFactory.class)
public interface RemoteDictService {

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @param source 内部认证请求头
     * @return 字典数据
     */
    @GetMapping("/dict/data/feignDictType")
    public R<List<SysDictData>> feignDictType(@RequestParam("dictType") String dictType, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
