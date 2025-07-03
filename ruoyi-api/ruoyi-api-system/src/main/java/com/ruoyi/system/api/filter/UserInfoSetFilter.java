package com.ruoyi.system.api.filter;

import com.ruoyi.common.core.context.SecurityContextHolder;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

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
        System.out.println("Dubbo拦截：" + Thread.currentThread());
        return invoker.invoke(invocation);
    }

}
