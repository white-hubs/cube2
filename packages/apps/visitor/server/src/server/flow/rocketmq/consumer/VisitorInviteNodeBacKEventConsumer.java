package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.guhecloud.cube.base.flow.client.ProcessInstanceClient;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message.VisitorInviteEventMessage;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.command.ProcessStartedMessageAttrCommand;
import instance.event.ProcessBackMessage;
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
 * @Date: 2025/2/18 19:01
 * @Description:
 */

@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = BusinessEventMessage.BACK_BACK + "_GROUP",
        topic = BusinessEventMessage.BACK_BACK, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteNodeBacKEventConsumer implements RocketMQListener<VisitorInviteEventMessage> {

    private final ProcessInstanceClient processInstanceClient;

    private final VisitorInviteService visitorInviteService;

    @Override
    public void onMessage(VisitorInviteEventMessage message) {

        try {
            ProcessBackMessage backMessage = JsonUtils.deserialize(message.getData(), ProcessBackMessage.class);
            SessionContextHolder.requestTenant(backMessage.getTenant());
            //判断当前业务系统是否存在表单存在则不后续审批逻辑
            ProcessStartedMessageAttrCommand processInstanceFormInfo = processInstanceClient.getProcessInstanceFormInfo(backMessage.getProcessInstanceId());
            if (Objects.nonNull(processInstanceFormInfo) || StringUtils.isBlank(backMessage.getSourceProcessId())) {
                return;
            }
            visitorInviteService.visitorInviteEventNodeBack(backMessage);
        } catch (Exception e) {
            log.error("访客邀约审批事件失败，失败原因：{}", e);
        }
    }
}

