package com.guhecloud.cube.base.flow.flowable.common;

import com.guhecloud.cube.base.flow.flowable.extension.node.ConfigSequenceFlowParseHandler;
import com.guhecloud.cube.base.flow.flowable.extension.node.ConfigServiceTaskParseHandler;
import com.guhecloud.cube.base.flow.flowable.extension.node.ConfigUserTaskParseHandler;
import lombok.RequiredArgsConstructor;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Arrays;

/**
 * @author pengjian
 * @date 2022/8/22 16:20
 * @description
 */
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@DependsOn({ "applicationContextHolder" })
public class FlowConfig implements ApplicationListener<ContextRefreshedEvent> {
    private final ProcessEngineConfigurationImpl configuration;
    private final ConfigUserTaskParseHandler configUserTaskParseHandler;
    private final ConfigServiceTaskParseHandler configServiceTaskParseHandler;
    private final ConfigSequenceFlowParseHandler configSequenceFlowParseHandler;

    //    @PostConstruct
    public void init() {
        configuration.setCustomDefaultBpmnParseHandlers(
                Arrays.asList(configUserTaskParseHandler, configServiceTaskParseHandler, configSequenceFlowParseHandler)
                                                       );
        //        ConfigTaskParseHandler.flag = false;
        ConfigServiceTaskParseHandler.flag = false;
        ConfigUserTaskParseHandler.flag = false;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        //防止重复执行
        //        ConfigTaskParseHandler.flag = true;
        ConfigServiceTaskParseHandler.flag = true;
        ConfigUserTaskParseHandler.flag = true;
    }

}
