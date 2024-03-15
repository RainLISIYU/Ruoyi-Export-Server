package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysTest;
import com.ruoyi.system.api.factory.RemoteBusinessFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 测试开发服务
 *
 * @author lsy
 */
@FeignClient(contextId = "remoteBusinessService", value = ServiceNameConstants.BUSINESS_SERVICE, fallbackFactory = RemoteBusinessFallbackFactory.class)
public interface RemoteBusinessService {

    @GetMapping("test/query")
    public R<List<SysTest>> list(@RequestParam(value = "name") String name, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
