package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.VisitorInviteStausEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.enumeration.VisitorFlowEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.event.ProcessBackMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/18 18:58
 * @Description:
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessBackMessage.TOPIC + "_GROUP",
        topic = ProcessBackMessage.TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteNodeBacKConsumer implements RocketMQListener<ProcessBackMessage> {
    private final VisitorInviteRecordService visitorInviteRecordService;

    @Override
    public void onMessage(ProcessBackMessage message) {
        BusinessEventMessage eventMessage = new BusinessEventMessage();
        eventMessage.setData(JSONObject.toJSONString(message));
        eventMessage.setBusinessEventType(message.getBusinessType());
        eventMessage.setTopic(BusinessEventMessage.BACK_BACK + ":" + VisitorFlowEnum.VISITOR_INVITE.name());
        eventMessage.setTimestamp(DateUtils.currentTimestamp());
        RocketMQContextHolder.send(BusinessEventMessage.TOPIC, eventMessage);
        visitorInviteRecordService
                .lambdaUpdate().eq(VisitorInviteRecordPO::getProcessInsId, message.getSourceProcessId())
                .set(VisitorInviteRecordPO::getStatus, VisitorInviteStausEnum.REJECTED.name())
                .update();
    }
}
