package com.guhecloud.cube.srv.gen.appraisals.server.consumer;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.appraisals.server.process.dao.FormAppraisalProcessMapper;
import com.guhecloud.cube.srv.gen.appraisals.server.process.entity.FormAppraisalProcess;
import com.guhecloud.platform.base.util.CollectionUtils;
import instance.event.ProcessDeleteMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 表单考核流程删除监听
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessDeleteMessage.TOPIC + "FORM_ASSESSMENT",
        topic = ProcessDeleteMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalProcessDeletedConsumer implements RocketMQListener<ProcessDeleteMessage> {

    private final FormAppraisalProcessMapper formAppraisalProcessMapper;

    @Override
    public void onMessage(ProcessDeleteMessage message) {
        if (message == null || CollectionUtils.isEmpty(message.getProcessInstanceId())) {
            return;
        }
        log.info("收到表单考核流程删除：{}", message.getProcessInstanceId());
        formAppraisalProcessMapper.delete(
                Wrappers.<FormAppraisalProcess>lambdaQuery()
                        .in(FormAppraisalProcess::getProcessInstanceId, message.getProcessInstanceId())
                                         );
    }
}
