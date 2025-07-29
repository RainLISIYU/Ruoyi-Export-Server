package com.ruoyi.system.api.factory;

import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.api.RemoteDictService;
import com.ruoyi.system.api.domain.SysDictData;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

/**
 * @author lsy
 * @description 字典远程调用降级
 * @date 2025/7/29
 */
public class RemoteDictFallbackFactory implements FallbackFactory<RemoteDictService> {

    @Override
    public RemoteDictService create(Throwable cause) {
        return new RemoteDictService() {
            @Override
            public R<List<SysDictData>> feignDictType(String dictType, String source) {
                return R.fail("远程字典调用失败");
            }
        };
    }
}
