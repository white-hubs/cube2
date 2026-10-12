package com.guhecloud.cube.srv.gen.loose.works.event;

import com.guhecloud.cube.srv.gen.loose.works.service.ProjectApplyService;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 工程请示申请单发起消息监听
 * @date 2023/9/15 14:43
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LOOSE_WORKS_APPLY_STARTED", topic = ProcessStartedMessage.TOPIC,
                         selectorExpression = "PROJECT_APPLY"
)
public class ProjectApplyStartedEvent implements RocketMQListener<ProcessStartedMessage> {
    private final ProjectApplyService projectApplyService;

    @Override
    public void onMessage(ProcessStartedMessage message) {
        if ("PROJECT_APPLY".equals(message.getBusinessType())) {
            log.info("工程请示申请单发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
            projectApplyService.addProjectApply(message);
        }
    }
}
