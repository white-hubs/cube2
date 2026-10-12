package com.guhecloud.cube.biz.emergency.server.time.rocketmq.consumer;

import com.guhecloud.cube.biz.emergency.server.time.domain.event.handler.DelEmergencyRuleEventHandler;
import com.guhecloud.cube.biz.emergency.server.time.rocketmq.message.DeleteEmergencyJobMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "emergency_job_del_group", topic = DeleteEmergencyJobMessage.TOPIC,
                         messageModel = MessageModel.BROADCASTING
)
public class EmergencyJobDeleteConsumer implements RocketMQListener<DeleteEmergencyJobMessage> {

    private final DelEmergencyRuleEventHandler delEmergencyRuleEventHandler;

    @Override
    public void onMessage(DeleteEmergencyJobMessage message) {
        delEmergencyRuleEventHandler.delRuleTimerTask(message.getJobIds());
    }
}
