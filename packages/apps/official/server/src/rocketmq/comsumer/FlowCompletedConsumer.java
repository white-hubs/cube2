package com.guhecloud.cube.srv.gen.official.management.server.rocketmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.FlowFormDataService;
import com.guhecloud.cube.srv.gen.official.management.server.po.FlowFormDataPO;
import instance.event.ProcessCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流程结束后监听
 */

@Slf4j
@Component("FLOW_COMPLETED_OFFICIAL")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = ProcessCompletedMessage.TOPIC + "_" + "OFFICIAL", topic = ProcessCompletedMessage.TOPIC)
public class FlowCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {
    private final FlowFormDataService flowFormDataService;

    @Override
    public void onMessage(ProcessCompletedMessage message) {
        if (!message.getBusinessId().getBusinessType().startsWith("OFFICIAL_")) {
            return;
        }
        log.info("行政模块，流程完成：" + JSONObject.toJSONString(message));
        FlowFormDataPO flowFormDataPO = new FlowFormDataPO();
        flowFormDataPO.setApplicantId(message.getStartUserId());
        flowFormDataPO.setApplyTime(message.getStartTime());
        flowFormDataPO.setApplyDoneTime(message.getCompletedTime());
        flowFormDataPO.setStatus(message.getResult().name());
        flowFormDataPO.setBusinessType(message.getBusinessId().getBusinessType());
        flowFormDataPO.setJsonForm(message.getData());
        flowFormDataService.addFlowFormDataAfterFlowCompleted(flowFormDataPO);
    }
}
