package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.enums.VisitorInviteStausEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.enumeration.VisitorFlowEnum;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.po.VisitorInvitePO;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitorInviteRecordPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.service.VisitorInviteRecordService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import instance.event.NodePassMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/14 10:42
 * @Description: 访客邀约访客填写补充信息后消费事件发送
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = NodePassMessage.TOPIC + "_GROUP",
        topic = NodePassMessage.TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteNodePassConsumer implements RocketMQListener<NodePassMessage> {
    private final VisitorInviteRecordService visitorInviteRecordService;
    private final FlowClient flowClient;

    @Override
    public void onMessage(NodePassMessage message) {
        BusinessEventMessage eventMessage = new BusinessEventMessage();
        eventMessage.setData(JSONObject.toJSONString(message));
        eventMessage.setBusinessEventType(message.getBusinessType());
        eventMessage.setTopic(BusinessEventMessage.PASS_TOPIC + ":" + VisitorFlowEnum.VISITOR_INVITE.name());
        eventMessage.setTimestamp(DateUtils.currentTimestamp());
        RocketMQContextHolder.send(BusinessEventMessage.TOPIC, eventMessage);
        String sourceProcessId = flowClient.getSourceProcessIdByProcessId(message.getProcessInstanceId());
        String processInstanceId = message.getProcessInstanceId();
        if (StringUtils.isNotBlank(sourceProcessId)) {
            processInstanceId = sourceProcessId;
        }
        List<VisitorInviteRecordPO> visitorInviteRecordPOS = visitorInviteRecordService.lambdaQuery()
                .eq(VisitorInviteRecordPO::getProcessInsId, processInstanceId)
                .list();
        if (CollectionUtils.isEmpty(visitorInviteRecordPOS)) {
            return;
        }
        VisitorInvitePO visitorInvitePO = JsonUtils.deserialize(message.getData(), VisitorInvitePO.class);
        visitorInviteRecordService.saveOrUpdateVisitorInviteRecord(visitorInvitePO, processInstanceId,
                                                                   VisitorInviteStausEnum.PROCESSING.name(), null);
    }
}
