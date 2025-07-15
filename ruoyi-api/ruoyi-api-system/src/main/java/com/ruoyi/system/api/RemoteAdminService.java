package com.ruoyi.system.api;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.constant.ServiceNameConstants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.domain.SysTcTest;
import com.ruoyi.system.api.factory.RemoteAdminFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(contextId = "remoteAdminService", value = ServiceNameConstants.TEST_SERVICE, fallbackFactory = RemoteAdminFallbackFactory.class)
public interface RemoteAdminService {

    @PostMapping("/tcTest/insert")
    R<Boolean> insert(SysTcTest tcTest, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}
