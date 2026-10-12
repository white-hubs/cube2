package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.VisitorInviteEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 15:59
 * @Description: 访客邀约事件发起消费
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = BusinessEventMessage.START_TOPIC + "_GROUP",
        topic = BusinessEventMessage.START_TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteEventConsumer implements RocketMQListener<VisitorInviteEventMessage> {

    private final VisitorInviteService visitorInviteService;

    private final ProcessInstanceClient processInstanceClient;

    @Override
    public void onMessage(VisitorInviteEventMessage message) {
        try {
            ProcessStartedMessage startedMessage = JsonUtils.deserialize(message.getData(), ProcessStartedMessage.class);
            SessionContextHolder.requestTenant(startedMessage.getTenant());
            //判断当前业务系统是否存在表单存在则不走后续生产新表单逻辑
            ProcessStartedMessageAttrCommand processInstanceFormInfo = processInstanceClient.getProcessInstanceFormInfo(startedMessage.getProcessInstanceId());
            if (Objects.nonNull(processInstanceFormInfo)) {
                return;
            }
            visitorInviteService.visitorInviteEventStart(startedMessage);
        } catch (Exception e) {
            log.error("访客邀约事件发起失败，失败原因：{}", e);
        }
    }
}
