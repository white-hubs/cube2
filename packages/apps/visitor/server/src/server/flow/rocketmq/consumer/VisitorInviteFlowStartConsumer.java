package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.enumeration.VisitorFlowEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 15:11
 * @Description: 访客邀约 发起审批
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessStartedMessage.TOPIC + "_VISITOR_INVITE",
        topic = ProcessStartedMessage.TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteFlowStartConsumer implements RocketMQListener<ProcessStartedMessage> {

    @Override
    public void onMessage(ProcessStartedMessage message) {
        // 自动发起不进行处理
        if (Objects.nonNull(message.getProcessVariables().get("sourceProcessId"))
            && StringUtils.isNotBlank(message.getProcessVariables().get("sourceProcessId").toString())) {
            return;
        }
        BusinessEventMessage processCompletedMessage = new BusinessEventMessage();
        processCompletedMessage.setData(JSONObject.toJSONString(message));
        processCompletedMessage.setBusinessEventType(message.getBusinessType());
        processCompletedMessage.setTopic(BusinessEventMessage.START_TOPIC + ":" + VisitorFlowEnum.VISITOR_INVITE.name());
        processCompletedMessage.setTimestamp(DateUtils.toTimestamp(message.getStartTime()));
        RocketMQContextHolder.send(BusinessEventMessage.TOPIC, processCompletedMessage);
    }
}
