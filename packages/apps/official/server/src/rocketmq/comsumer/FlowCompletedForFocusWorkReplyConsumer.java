package com.guhecloud.cube.srv.gen.official.management.server.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.FocusWorkCommandService;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkReplyCommand;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流程结束后监听
 */

@Slf4j
@Component("FLOW_COMPLETED_OFFICIAL_FOCUS_WORK_REPLY")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "OFFICIAL_FOCUS_WORK_REPLY",
                         topic = ProcessCompletedMessage.TOPIC, selectorExpression = "OFFICIAL_FOCUS_WORK_REPLY"
)
public class FlowCompletedForFocusWorkReplyConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final FocusWorkCommandService focusWorkCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("督办事项反馈审批完成：" + JSONObject.toJSONString(message));
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            FocusWorkReplyCommand focusWorkReplyCommand = JsonUtils.deserialize(message.getData(), FocusWorkReplyCommand.class);
            focusWorkReplyCommand.setApplicantId(message.getStartUserId());
            focusWorkReplyCommand.setApplyTime(message.getStartTime());
            focusWorkReplyCommand.setApplyDoneTime(message.getCompletedTime());
            focusWorkCommandService.addReplyLogAndUpdateFocusWork(focusWorkReplyCommand);
        }
    }
}
