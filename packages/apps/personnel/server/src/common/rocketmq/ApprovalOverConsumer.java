package com.guhecloud.cube.srv.gen.personnel.management.server.common.rocketmq;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.FlowFormDataCommandService;
import com.guhecloud.cube.srv.gen.personnel.management.server.common.commandgateway.command.SubmitFlowFormDataCommand;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 审批通过后，消费
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "PERSONNEL", topic = ProcessCompletedMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class ApprovalOverConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final FlowFormDataCommandService commandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        String businessType = message.getBusinessId().getBusinessType();
        // 只监听审批通过或者拒绝的
        if (businessType.startsWith("PERSONNEL_") &&
            (FlowResult.PASS.name().equals(message.getResult().name()) || FlowResult.REJECT.name().equals(message.getResult().name()))) {
            log.info("人事审批通过：" + JSONObject.toJSONString(message));
            commandService.save(SubmitFlowFormDataCommand.builder()
                                        .applicantId(message.getStartUserId())
                                        .applyTime(message.getStartTime())
                                        .applyDoneTime(message.getCompletedTime())
                                        .status(message.getResult().name())
                                        .businessType(message.getBusinessId().getBusinessType())
                                        .tenant(message.getTenant())
                                        .jsonForm(message.getData())
                                        .build());
        }
    }
}
