package com.guhecloud.cube.srv.gen.loose.works.event;

import com.guhecloud.cube.srv.gen.loose.works.service.ProjectReformService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 10:15
 * @Description:
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LOOSE_WORKS_REFORM_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "PROJECT_REFORM"
)
public class ProjectReformCompletedEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final ProjectReformService projectReformService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if ("PROJECT_REFORM".equals(message.getBusinessId().getBusinessType())) {
            log.info("工程整改申请单审批完成:{},data:{}", message.getProcessInstanceId(), message.getData());
            projectReformService.reformCompleted(message);
        }
    }
}
