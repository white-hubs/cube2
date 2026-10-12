package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.event;

import com.guhecloud.cube.srv.gen.maintain.server.common.enumeration.MaintainApplicationEnum;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wql
 * @describe 隐患单审批完成消息
 * @date 2023/11/9 9:48
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "HIDDEN_DANGER_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "HIDDENDANGER"
)
public class HiddenDangerCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {
    private final HiddenDangerNewService hiddenDangerNewService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (MaintainApplicationEnum.HIDDENDANGER.name().equals(message.getBusinessId().getBusinessType())) {
            log.info("隐患排查流程审批结束:{},data:{},result:{}", message.getProcessInstanceId(), message.getData(), message.getResult()
                    .name());
            hiddenDangerNewService.updateResult(message.getProcessInstanceId(), message.getData(), message.getResult());
        }
    }
}
