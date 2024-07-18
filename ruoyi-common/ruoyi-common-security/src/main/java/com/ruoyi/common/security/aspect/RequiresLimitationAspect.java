package com.ruoyi.common.security.aspect;

import com.ruoyi.common.redis.service.RedisService;
import com.ruoyi.common.security.annotation.RequiresLimitation;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

/**
 * @author lsy
 * @description 限制提交切面
 * @date 2024/7/17
 */
@Aspect
@Component
public class RequiresLimitationAspect implements Ordered {

    private static final String KEY_PREFIX = "requiresLimitation:";

    @Autowired
    private RedisService redisService;

    @Before("@annotation(requiresLimitation)")
    public void before(JoinPoint joinPoint, RequiresLimitation requiresLimitation) throws Throwable
    {
        //获取注解参数
        String value = requiresLimitation.value();
        long time = requiresLimitation.time();
        //获取请求信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        String methodName = signature.getName();
        //根据方法名称与参数获取key
        Object arg = args[0];
        StringBuilder key = new StringBuilder(KEY_PREFIX).append(arg.getClass().getName()).append(".").append(methodName).append(":");
        if (StringUtil.isNotBlank(value) && args.length == 1) {
            //获取与配置相符的字段值
            Class<?> aClass = arg.getClass();
            for (Field field : aClass.getDeclaredFields()) {
                if (value.equals(field.getName())) {
                    field.setAccessible(true);
                    key.append(field.get(arg));
                }
            }
        }
        //判断是否重复提交
        if (redisService.hasKey(key.toString())) {
            throw new RuntimeException("请勿重复提交");
        } else {
            redisService.setCacheObject(key.toString(), 1, time, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
