package com.guhecloud.cube.srv.gen.loose.works.event;

import com.guhecloud.cube.srv.gen.loose.works.event.message.ProcessStartedMessageAttr;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectReformService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/13 9:40
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LOOSE_WORKS_REFORM_STARTED", topic = ProcessStartedMessageAttr.TOPIC,
                         selectorExpression = "PROJECT_REFORM"
)
public class ProjectReformStartedEvent implements RocketMQListener<ProcessStartedMessageAttr> {
    private final ProjectReformService projectReformService;

    @Override
    public void onMessage(ProcessStartedMessageAttr message) {
        if ("PROJECT_REFORM".equals(message.getBusinessType())) {
            log.info("工程整改申请单发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
            projectReformService.addProjectReform(message, message.getInstanceName());
        }
    }
}
