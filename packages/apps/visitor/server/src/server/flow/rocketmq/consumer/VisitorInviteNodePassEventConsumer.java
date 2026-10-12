package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.VisitorInviteEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.NodePassMessage;
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
 * @Date: 2025/2/14 10:48
 * @Description: 访客填写补充信息后回调到主工单进行事件消费
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = BusinessEventMessage.PASS_TOPIC + "_GROUP",
        topic = BusinessEventMessage.PASS_TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteNodePassEventConsumer implements RocketMQListener<VisitorInviteEventMessage> {

    private final ProcessInstanceClient processInstanceClient;

    private final VisitorInviteService visitorInviteService;

    @Override
    public void onMessage(VisitorInviteEventMessage message) {

        try {
            NodePassMessage passMessage = JsonUtils.deserialize(message.getData(), NodePassMessage.class);
            SessionContextHolder.requestTenant(passMessage.getTenant());
            //判断当前业务系统是否存在表单存在则不后续审批逻辑
            ProcessStartedMessageAttrCommand processInstanceFormInfo = processInstanceClient.getProcessInstanceFormInfo(passMessage.getProcessInstanceId());
            if (Objects.nonNull(processInstanceFormInfo) || StringUtils.isBlank(passMessage.getSourceProcessId())) {
                return;
            }
            visitorInviteService.visitorInviteEventNodePass(passMessage);
        } catch (Exception e) {
            log.error("访客邀约审批事件失败，失败原因：{}", e);
        }
    }
}
