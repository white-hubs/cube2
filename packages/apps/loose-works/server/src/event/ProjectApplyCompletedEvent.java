package com.guhecloud.cube.srv.gen.loose.works.event;

import com.guhecloud.cube.srv.gen.loose.works.service.ProjectApplyService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 工程请示申请单审批通过消息监听
 * @date 2023/9/15 15:09
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LOOSE_WORKS_APPLY_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "PROJECT_APPLY"
)
public class ProjectApplyCompletedEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final ProjectApplyService projectApplyService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if ("PROJECT_APPLY".equals(message.getBusinessId().getBusinessType())) {
            log.info("请示申请单审批完成:{},data:{}", message.getProcessInstanceId(), message.getData());
            projectApplyService.approveCompleted(message);
        }
    }
}
