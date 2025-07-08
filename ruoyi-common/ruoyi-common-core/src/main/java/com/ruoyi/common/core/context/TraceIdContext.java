package com.ruoyi.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.uuid.UUID;
import org.slf4j.MDC;

/**
 * @author lsy
 * @description 链路追踪上下文
 * @date 2025/7/8
 */
public class TraceIdContext {

    private static final TransmittableThreadLocal<String> traceIdThreadLocal = new TransmittableThreadLocal<>();

    /**
     * 获取traceId
     *
     * @return traceId
     */
    public static String getTraceId() {
        return traceIdThreadLocal.get();
    }

    /**
     * 设置traceId
     *
     * @param traceId 链路id
     */
    public static void setTraceId(String traceId) {
        if (StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(SecurityConstants.TRACE_ID, traceId);
        }
        traceIdThreadLocal.set(traceId);
    }

    /**
     * 清除缓存
     */
    public static void remove() {
        traceIdThreadLocal.remove();
        MDC.clear();
    }

}
