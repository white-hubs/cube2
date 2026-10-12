package com.guhecloud.cube.biz.daily.task.domain.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.daily.task.domain.listener.adapter.ProcessDeleteMessage;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordWorkOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听隐患/故障单删除消息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "DAILY_FAULT_HIDDEN_DELETE", topic = ProcessDeleteMessage.TOPIC,
                         selectorExpression = "FAULT || HIDDENDANGER"
)
public class DailyFaultHiddenDangerDeleteEvent implements RocketMQListener<ProcessDeleteMessage> {
    private final DailyRecordWorkOrderMapper dailyRecordWorkOrderMapper;

    @Override
    public void onMessage(ProcessDeleteMessage message) {
        log.info("隐患/故障删除流程实例:{}", message.getProcessInstanceId());
        dailyRecordWorkOrderMapper.delete(Wrappers.<DailyRecordWorkOrderPO>lambdaQuery()
                                                  .in(DailyRecordWorkOrderPO::getProcessInstanceId, message.getProcessInstanceId()));
    }
}
