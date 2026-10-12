package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.consumer;

import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.flow.service.VisitorInviteService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/18 19:18
 * @Description: 流程审批完成消费
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_GROUP",
        topic = ProcessCompletedMessage.TOPIC, selectorExpression = "VISITOR_INVITE"
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisitorInviteNodeCompleteConsumer implements RocketMQListener<ProcessCompletedMessage> {

    private final VisitorInviteService visitorInviteService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {

        try {

            SessionContextHolder.requestTenant(message.getTenant());
            if (FlowResult.PASS.equals(message.getResult())) {
                visitorInviteService.visitorInviteEventNodeComplete(message);
            }
        } catch (Exception e) {
            log.error("访客邀约审批事件失败，失败原因：{}", e);
        }
    }
}
