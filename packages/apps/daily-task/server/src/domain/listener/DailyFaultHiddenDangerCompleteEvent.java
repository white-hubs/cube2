package com.guhecloud.cube.biz.daily.task.domain.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.daily.task.domain.listener.adapter.ProcessCompletedMessage;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyRecordWorkOrderPO;
import com.guhecloud.cube.biz.daily.task.record.dao.DailyRecordWorkOrderMapper;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 监听隐患和故障单审批完成消息
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "DAILY_FAULT_HIDDEN_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "FAULT || HIDDENDANGER"
)
public class DailyFaultHiddenDangerCompleteEvent implements RocketMQListener<ProcessCompletedMessage> {

    private final DailyRecordWorkOrderMapper dailyRecordWorkOrderMapper;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (message == null || StringUtils.isBlank(message.getProcessInstanceId()) || message.getResult() == null) {
            log.error("隐患/故障监听通过,参数错误:{}", message);
            return;
        }
        log.info("隐患/故障单审批完成:{},result:{}", message.getProcessInstanceId(), message.getResult().name());
        dailyRecordWorkOrderMapper.update(Wrappers.<DailyRecordWorkOrderPO>lambdaUpdate()
                                                  .set(DailyRecordWorkOrderPO::getProcessInstanceStatus, message.getResult().name())
                                                  .eq(DailyRecordWorkOrderPO::getProcessInstanceId, message.getProcessInstanceId()));
    }
}
