package com.guhecloud.cube.biz.daily.task.task.consumer;

import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.mdata.client.rocketmq.message.GridAreaMemberSetMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = GridAreaMemberSetMessage.TOPIC + "_DAILY_TASK",
                         topic = GridAreaMemberSetMessage.TOPIC)
public class DailyTaskGridAreaMemberSetConsumer implements RocketMQListener<GridAreaMemberSetMessage> {

    private final DailyTaskService dailyTaskService;

    @Override
    public void onMessage(GridAreaMemberSetMessage message) {
        try {
            if (message != null && message.getTenant() != null) {
                SessionContextHolder.requestTenant(message.getTenant());
            }
            dailyTaskService.handleGridAreaMemberSet(message);
        } catch (Exception e) {
            log.error("daily task grid area member set error: {}", e.getMessage(), e);
        }
    }
}
