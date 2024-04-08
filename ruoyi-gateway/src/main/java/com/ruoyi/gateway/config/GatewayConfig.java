package com.ruoyi.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.ruoyi.gateway.handler.SentinelFallbackHandler;

import java.util.HashSet;
import java.util.Set;

/**
 * 网关限流配置
 * 
 * @author ruoyi
 */
@Configuration
public class GatewayConfig
{
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler()
    {
        return new SentinelFallbackHandler();
    }

//    @Bean
//    @Order(-1)
//    public GlobalFilter sentinelGatewayFilter(){
//        return new SentinelGatewayFilter();
//    }

    @PostConstruct
    public void doInit(){
        //加载网关限流规则
        initGatewayRules();
    }

    /**
     * 网关限流规则
     */
    private void initGatewayRules(){
        Set<GatewayFlowRule> rules = new HashSet<>();
        rules.add(new GatewayFlowRule("ruoyi-system").setCount(3).setIntervalSec(3).setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        //加载网关限流规则
        GatewayRuleManager.loadRules(rules);
        //加载限流分组
        initCustomizedApis();
    }

    /**
     * 限流分组
     */
    private void initCustomizedApis(){
        Set<ApiDefinition> definitions = new HashSet<>();
        //system分组
        ApiDefinition sysApi = new ApiDefinition("system-api").setPredicateItems(new HashSet<ApiPredicateItem>(){
            private static final long serialVersionUID = 1L;
            {
                add(new ApiPathPredicateItem().setPattern("/system/user/list").setMatchStrategy(SentinelGatewayConstants.URL_MATCH_STRATEGY_PREFIX));
            }
        });
        definitions.add(sysApi);
        //加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}