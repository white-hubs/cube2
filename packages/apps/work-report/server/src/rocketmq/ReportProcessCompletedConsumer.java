package com.guhecloud.cube.srv.gen.reportmanagement.server.rocketmq;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.reportmanagement.server.dao.ReportMapper;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.platform.base.context.SessionContextHolder;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 工作汇报流程完成消息消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_REPORT_COMPLETED", topic = ProcessCompletedMessage.TOPIC,
                         selectorExpression = "REPORT", selectorType = SelectorType.TAG
)
public class ReportProcessCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final ReportMapper reportMapper;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        log.info("工作报告流程结束消息消费开始");
        SessionContextHolder.clearSessionContext();
        try {
            SessionContextHolder.requestTenant(message.getTenant());
            Report existReport = this.reportMapper
                    .selectOne(Wrappers.<Report>lambdaQuery().eq(Report::getProcessInsId, message.getProcessInstanceId()));
            if (Objects.nonNull(existReport)) {
                existReport.setStatus(message.getResult());
                existReport.setProcessResult(message.getResult().getDesc());
                reportMapper.updateById(existReport);
            }

        } catch (Exception exception) {
            log.error("工作报告流程结束消息消费失败，失败原因：{}", exception);
        }
    }
}
