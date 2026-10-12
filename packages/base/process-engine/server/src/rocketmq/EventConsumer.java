package com.guhecloud.cube.base.flow.rocketmq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.context.interfaces.vo.FormFlowDetailVO;
import com.guhecloud.cube.base.flow.context.querygateway.FormFlowBusinessQueryService;
import com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService;
import com.guhecloud.cube.base.flow.flowable.process.application.event.publisher.ProcessCompletedMessage;
import com.guhecloud.cube.base.flow.rocketmq.message.ProcessEventStartMessage;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  14:39
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessEventStartMessage.TOPIC + "_SYNC_ALL",
        topic = ProcessEventStartMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventConsumer implements RocketMQListener<ProcessEventStartMessage> {
    private final FormFlowBusinessQueryService formFlowBusinessQueryService;
    private final SubmitFormFlowCommandService submitFormFlowCommandService;
    private final UserClient userClient;

    @Override
    public void onMessage(ProcessEventStartMessage message) {
        try {
            ProcessCompletedMessage processCompletedMessage = JsonUtils.deserialize(message.getData(), ProcessCompletedMessage.class);
            SessionContextHolder.requestTenant(processCompletedMessage.getTenant());
            FormFlowDetailVO formFlowDetailVO = formFlowBusinessQueryService.getByBusinessType(message.getBusinessEventType());
            SubmitInitFormCommand command = new SubmitInitFormCommand();
            command.setType("FLOW_FORM");
            command.setApplication(formFlowDetailVO.getApplication());
            command.setApplicationName(formFlowDetailVO.getApplicationName());
            command.setBusinessType(message.getBusinessEventType());
            command.setFlowKey(formFlowDetailVO.getFlowKey());
            command.setFormKey(formFlowDetailVO.getFormKey());
            command.setFormDefinitionId(formFlowDetailVO.getFormDefinitionId());
            command.setFormDefinitionVersion(formFlowDetailVO.getFormDefinitionVersion());
            //data数据解析
            Map<String, Object> dataObject = JsonUtils.deserialize(processCompletedMessage.getData(), new TypeReference<Map<String, Object>>() {});
            if (MapUtils.isEmpty(dataObject)) {
                throw new RuntimeException("data数据为空");
            }
            //设置发起人信息
            UserDTO user = userClient.getUserByMobile(processCompletedMessage.getMobile());
            SecurityContextHolder.setPrincipal(Principal.builder()
                                                       .id(user.getId())
                                                       .name(user.getName())
                                                       .type(Principal.Type.USER)
                                                       .build());
            command.setBackLogTitle(user.getName() + "发起的" + formFlowDetailVO.getBusinessTypeName());

            command.setData(processCompletedMessage.getSourceData());
            Map<String, Object> variables = new HashMap<>();
            variables.put("resource", "MANUAL_SUBMISSION");
            command.setVariables(variables);
            //发起流程
            submitFormFlowCommandService.submitFirstForm(command);
        } catch (Exception exception) {
            log.error("流程启动失败", exception);
        }
    }
}
