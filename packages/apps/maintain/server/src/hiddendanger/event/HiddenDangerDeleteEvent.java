package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.event;

import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerNewService;
import instance.event.ProcessDeleteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/21 15:48
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "HIDDEN_DANGER_DELETED", topic = ProcessDeleteMessage.TOPIC,
                         selectorExpression = "HIDDENDANGER"
)
public class HiddenDangerDeleteEvent implements RocketMQListener<ProcessDeleteMessage> {
    private final HiddenDangerNewService hiddenDangerNewService;

    @Override
    public void onMessage(ProcessDeleteMessage message) {
        hiddenDangerNewService.deleteHiddenDanger(message.getProcessInstanceId());
    }
}
