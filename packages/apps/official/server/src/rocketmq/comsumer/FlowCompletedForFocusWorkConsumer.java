package com.guhecloud.cube.srv.gen.official.management.server.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.util.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.FocusWorkCommandService;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkCommand;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程结束后监听
 */

@Slf4j
@Component("FLOW_COMPLETED_OFFICIAL_FOCUS_WORK")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "OFFICIAL_FOCUS_WORK", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "OFFICIAL_FOCUS_WORK"
)
public class FlowCompletedForFocusWorkConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final FocusWorkCommandService focusWorkCommandService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("督办事项审批完成：" + JSONObject.toJSONString(message));
        if (FlowResult.PASS.name().equals(message.getResult().name())) {
            JSONObject jsonData = JSONObject.parseObject(message.getData());
            FocusWorkCommand focusWork = JSONObject.parseObject(message.getData(), FocusWorkCommand.class);
            focusWork.setCompleteTimeStr(jsonData.getString("completeTime"));
            focusWorkCommandService.addFocusWork(formatFocusWorkInfo(focusWork, message));
        }
    }

    private FocusWorkCommand formatFocusWorkInfo(FocusWorkCommand focusWork, ProcessCompletedMessage message) {
        //部门
        if (StringUtils.isNotBlank(focusWork.getDeptId())) {
            List<String> deptIds = JsonUtils.deserialize(focusWork.getDeptId(), new TypeReference<List<String>>() {
            });
            focusWork.setDeptId(deptIds.stream().collect(Collectors.joining(",")));
        }
        //第一责任人
        if (StringUtils.isNotBlank(focusWork.getFirstUserId())) {
            List<String> userIds = JsonUtils.deserialize(focusWork.getFirstUserId(), new TypeReference<List<String>>() {
            });
            focusWork.setFirstUserId(userIds.stream().collect(Collectors.joining(",")));
        }
        //第二责任人
        if (StringUtils.isNotBlank(focusWork.getSecondUserId())) {
            List<String> userIds = JsonUtils.deserialize(focusWork.getSecondUserId(), new TypeReference<List<String>>() {
            });
            focusWork.setSecondUserId(userIds.stream().collect(Collectors.joining(",")));
        }
        //督办下发人
        if (StringUtils.isNotBlank(focusWork.getFocusWorkStarterId())) {
            List<String> userIds = JsonUtils.deserialize(focusWork.getFocusWorkStarterId(), new TypeReference<List<String>>() {
            });
            focusWork.setFocusWorkStarterId(userIds.stream().collect(Collectors.joining(",")));
        }

        focusWork.setApplicantId(message.getStartUserId());
        focusWork.setApplyTime(message.getStartTime());
        focusWork.setApplyDoneTime(message.getCompletedTime());
        focusWork.setProcessInstanceId(message.getProcessInstanceId());
        return focusWork;
    }
}
