package com.guhecloud.cube.srv.gen.loose.works.event;

import com.guhecloud.cube.srv.gen.loose.works.service.ProjectSignContractService;
import instance.event.ProcessStartedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 工程签约申请单发起成功消息
 * @date 2023/9/15 15:18
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "LOOSE_WORKS_SIGN_CONTRACT_STARTED", topic = ProcessStartedMessage.TOPIC,
                         selectorExpression = "PROJECT_SIGN_CONTRACT"
)
public class ProjectSignContractStartedEvent implements RocketMQListener<ProcessStartedMessage> {
    private final ProjectSignContractService projectSignContractService;

    @Override
    public void onMessage(ProcessStartedMessage message) {
        if ("PROJECT_SIGN_CONTRACT".equals(message.getBusinessType())) {
            log.info("工程签约申请单发起成功:{},data:{}", message.getProcessInstanceId(), message.getData());
            projectSignContractService.addSignContract(message);
        }
    }
}
