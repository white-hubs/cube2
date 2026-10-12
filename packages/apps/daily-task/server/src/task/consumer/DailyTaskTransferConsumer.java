package com.guhecloud.cube.biz.daily.task.task.consumer;

import com.guhecloud.cube.biz.daily.task.param.DailyTaskOperationParam;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import com.guhecloud.cube.biz.work.center.client.message.WorkTaskTransferMessage;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/17 20:06
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = WorkTaskTransferMessage.TOPIC + "_DAILY_TASK",
                         topic = WorkTaskTransferMessage.TOPIC,
                         selectorExpression = "DAILY_TASK"
)
public class DailyTaskTransferConsumer implements RocketMQListener<WorkTaskTransferMessage> {
    private final DailyTaskService dailyTaskService;

    @Override
    public void onMessage(WorkTaskTransferMessage message) {
        try {
            SessionContextHolder.requestTenant(message.getTenantId());
            DailyTaskOperationParam param = new DailyTaskOperationParam();
            param.setTaskIdList(message.getBusinessKeyList().stream().map(Long::valueOf).collect(Collectors.toList()));
            param.setOperatorId(message.getUserId());
            param.setTransferUserId(message.getTargetId());
            dailyTaskService.transferDailyTask(param);
        } catch (Exception e) {
            log.error(" daily task transfer error: {}", e.getMessage(), e);
        }

    }
}
