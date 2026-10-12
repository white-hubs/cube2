package com.guhecloud.cube.base.flow.context.config;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.flow.flowable.common.event.GlobalEventListener;
import lombok.extern.slf4j.Slf4j;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.bpmn.behavior.VariablesPropagator;
import org.activiti.engine.impl.event.EventSubscriptionPayloadMappingProvider;
import org.activiti.runtime.api.impl.ExtensionsVariablesMappingProvider;
import org.activiti.runtime.api.impl.MappingAwareActivityBehaviorFactory;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.activiti.spring.process.ProcessVariablesInitiator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/27 11:07
 * @Description:
 */
@Configuration
@Slf4j
@Primary
public class ActivityDefCacheConfiguration implements ProcessEngineConfigurationConfigurer {
    /**
     * Spring应用上下文环境
     */
    private final GlobalEventListener globalEventListener;
    private final ApplicationContext applicationContext;
    private final ExtensionsVariablesMappingProvider variablesMappingProvider;
    private final ProcessVariablesInitiator processVariablesInitiator;
    private final VariablesPropagator variablesPropagator;
    private final EventSubscriptionPayloadMappingProvider eventSubscriptionPayloadMappingProvider;

    /**
     * 构造器注入
     *
     * @param applicationContext
     * @param variablesMappingProvider
     * @param processVariablesInitiator
     * @param eventSubscriptionPayloadMappingProvider
     *
     * @return
     *
     * @author yang.xuefeng
     * @date 2021/05/07
     */
    public ActivityDefCacheConfiguration(GlobalEventListener globalEventListener, ApplicationContext applicationContext,
                                         ExtensionsVariablesMappingProvider variablesMappingProvider,
                                         ProcessVariablesInitiator processVariablesInitiator, VariablesPropagator variablesPropagator,
                                         EventSubscriptionPayloadMappingProvider eventSubscriptionPayloadMappingProvider
                                        ) {
        this.globalEventListener = globalEventListener;
        this.applicationContext = applicationContext;
        this.variablesMappingProvider = variablesMappingProvider;
        this.processVariablesInitiator = processVariablesInitiator;
        this.variablesPropagator = variablesPropagator;
        this.eventSubscriptionPayloadMappingProvider = eventSubscriptionPayloadMappingProvider;
    }

    /**
     * 加载流程redis缓存
     */
    @Bean(ActivityDeploymentCache.DEFAULT_ACTIVITY_DEF_CACHE_BEAN_NAME)
    @ConditionalOnMissingBean(name = ActivityDeploymentCache.DEFAULT_ACTIVITY_DEF_CACHE_BEAN_NAME)
    public ActivityDeploymentCache activitiDeploymentCache(RedisTemplate<String, byte[]> redisTemplate) {
        return new ActivityDeploymentCache(redisTemplate, new BpmnXMLConverter());
    }

    /**
     * 自定义activiti引擎配置类
     *
     * @param processEngineConfiguration
     */
    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListener = new ArrayList<>();
        //配置全局监听器
        activitiEventListener.add(globalEventListener);
        processEngineConfiguration.setEventListeners(activitiEventListener);
        processEngineConfiguration.setEventSubscriptionPayloadMappingProvider(eventSubscriptionPayloadMappingProvider);
        processEngineConfiguration.setIdGenerator(IdWorker::getIdStr);
        processEngineConfiguration.setActivityBehaviorFactory(
                new MappingAwareActivityBehaviorFactory(variablesMappingProvider, processVariablesInitiator, variablesPropagator));

        processEngineConfiguration.setProcessDefinitionCache(applicationContext.getBean(ActivityDeploymentCache.class));

    }

    @Bean
    public UserGroupManager userGroupManager() {
        return new UserGroupManager() {

            @Override
            public List<String> getUsers() {
                return null;
            }

            @Override
            public List<String> getUserRoles(String username) {
                return null;
            }

            @Override
            public List<String> getUserGroups(String username) {
                return null;
            }

            @Override
            public List<String> getGroups() {
                return null;
            }
        };

    }

}
