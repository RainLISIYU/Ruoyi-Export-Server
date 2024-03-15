//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.baomidou.mybatisplus.autoconfigure;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdentifierGeneratorAutoConfiguration {
    public IdentifierGeneratorAutoConfiguration() {
    }

    @Configuration
    @EnableConfigurationProperties({MybatisPlusProperties.class})
    @ConditionalOnClass({InetUtils.class})
    public static class InetUtilsAutoConfig {
        private final InetUtils inetUtils;
        private final MybatisPlusProperties properties;

        public InetUtilsAutoConfig(InetUtils inetUtils, MybatisPlusProperties properties) {
            this.inetUtils = inetUtils;
            this.properties = properties;
        }

        @Bean
        @ConditionalOnMissingBean
        public IdentifierGenerator identifierGenerator() {
            GlobalConfig globalConfig = this.properties.getGlobalConfig();
            Long workerId = globalConfig.getWorkerId();
            Long datacenterId = globalConfig.getDatacenterId();
            return workerId != null && datacenterId != null ? new DefaultIdentifierGenerator(workerId, datacenterId) : new DefaultIdentifierGenerator(this.inetUtils.findFirstNonLoopbackAddress());
        }
    }
}
