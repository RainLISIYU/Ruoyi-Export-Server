package com.ruoyi.system.api.filter;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @author lsy
 * @description Dubbo获取用户信息过滤器
 * @date 2025/6/16
 */
@Activate(group = CommonConstants.PROVIDER)
public class UserInfoGetFilter implements Filter {


    private static final ThreadLocal<String> securityContext = new TransmittableThreadLocal<>();

    public static String getUsername() {
        return securityContext.get();
    }

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String username = RpcContext.getServerAttachment().getAttachment("username");
        System.out.println("Dubbo提供者拦截：" + username);
        securityContext.set( username );
        return invoker.invoke(invocation);
    }

}
