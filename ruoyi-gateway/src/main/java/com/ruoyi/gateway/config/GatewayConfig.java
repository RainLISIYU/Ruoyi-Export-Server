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
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.ruoyi.gateway.handler.SentinelFallbackHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        // 限流规则
        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(new GatewayFlowRule("ruoyi-system").setCount(100).setIntervalSec(100).setControlBehavior(RuleConstant.CONTROL_BEHAVIOR_DEFAULT));
        //加载网关限流规则
        GatewayRuleManager.loadRules(rules);
        //加载限流分组
        initCustomizedApis();
        // 熔断配置
        List<DegradeRule> degradeRules = new ArrayList<>();
        DegradeRule degradeRule = new DegradeRule();
        degradeRule.setResource("com.ruoyi.system.api::getInfo()");
        degradeRule.setGrade(CircuitBreakerStrategy.ERROR_RATIO.getType());
        degradeRule.setCount(0.7);
        degradeRule.setMinRequestAmount(100);
        degradeRule.setStatIntervalMs(30000);
        degradeRule.setTimeWindow(10);
        degradeRules.add(degradeRule);
        DegradeRuleManager.loadRules(degradeRules);
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
//        definitions.add(sysApi);
        //加载限流分组
        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
    }
}