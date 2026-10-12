package com.guhecloud.cube.srv.gen.maintain.server.electron.event;

import com.guhecloud.cube.srv.gen.maintain.server.electron.service.PatrolTaskLocationService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.rocketmq.message.HiddenDangerPatrolMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = HiddenDangerPatrolMessage.TOPIC + "RESULT",
                         topic = HiddenDangerPatrolMessage.TOPIC
)
public class HiddenResultEvent implements RocketMQListener<HiddenDangerPatrolMessage> {
    private final PatrolTaskLocationService patrolTaskLocationService;

    @Override
    public void onMessage(HiddenDangerPatrolMessage message) {
        log.info("监听隐患工单");
        patrolTaskLocationService.updateHidden(message.getHiddenDangerIds());
    }
}
