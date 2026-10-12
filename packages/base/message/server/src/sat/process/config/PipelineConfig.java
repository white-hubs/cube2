package com.guhecloud.cube.base.message2.server.sat.process.config;

import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessController;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessTemplate;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.AfterMsgMqSendAction;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.AssembleMsgInfoAction;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.AssembleMsgRepositoryAction;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.PreMsgParamAction;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.PreUserIgnoredAction;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lanchun
 * @date 2023/5/8 11:45
 */
@Configuration
public class PipelineConfig {

    @Bean("commandSendProcessTemplate")
    public ProcessTemplate commandSendProcessTemplate(PreMsgParamAction msgSendParamProcessHandler,
                                                      AssembleMsgInfoAction assembleMsgInfoAction,
                                                      AssembleMsgRepositoryAction assembleMsgRepositoryAction,
                                                      PreUserIgnoredAction preUserIgnoredAction,
                                                      AfterMsgMqSendAction afterMsgMqSendAction
                                                     ) {
        ProcessTemplate processTemplate = new ProcessTemplate();
        //添加业务处理执行器
        List<BusinessProcess<MsgSendContext>> businessProcesses = new ArrayList<>();
        businessProcesses.add(msgSendParamProcessHandler);
        businessProcesses.add(assembleMsgInfoAction);
        businessProcesses.add(assembleMsgRepositoryAction);
        businessProcesses.add(preUserIgnoredAction);
        businessProcesses.add(afterMsgMqSendAction);
        processTemplate.setProcesses(businessProcesses);
        return processTemplate;
    }

    /**
     * 流程控制器
     *
     * @return
     */
    @Bean
    public ProcessController processController(ProcessTemplate processTemplate) {
        ProcessController processController = new ProcessController();
        //添加流程处理模板
        processController.setProcessTemplateMap(Collections.singletonMap("commandSendProcessTemplate", processTemplate));
        return processController;
    }
}
