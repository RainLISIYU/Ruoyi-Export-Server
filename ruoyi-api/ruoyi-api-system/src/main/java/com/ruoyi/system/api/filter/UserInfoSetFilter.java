package com.ruoyi.system.api.filter;

import com.ruoyi.common.core.constant.SecurityConstants;
import com.ruoyi.common.core.context.SecurityContextHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;
import org.slf4j.MDC;

/**
 * @author lsy
 * @description Dubbo用户名传递拦截器
 * @date 2025/6/16
 */
@Activate(group = CommonConstants.CONSUMER)
public class UserInfoSetFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getClientAttachment().setAttachment("username", SecurityContextHolder.getUserName());
        RpcContext.getClientAttachment().setAttachment(SecurityConstants.TRACE_ID, MDC.get(SecurityConstants.TRACE_ID));
        System.out.println("Dubbo拦截：" + Thread.currentThread());
        return invoker.invoke(invocation);
    }

}
