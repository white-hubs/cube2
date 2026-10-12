package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.cube.srv.gen.business.info.sync.server.enumeration.BusinessTypeEnum;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.BusinessEventMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.ProcessCompletedMessage;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  11:36
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
@RocketMQMessageListener(
        consumerGroup = ProcessCompletedMessage.TOPIC + "_SYNC_ALL",
        topic = ProcessCompletedMessage.TOPIC
)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncFlowCompletedConsumer implements RocketMQListener<ProcessCompletedMessage> {
    @Override
    public void onMessage(ProcessCompletedMessage message) {
        BusinessTypeEnum businessTypeEnum = BusinessTypeEnum.getbusinessTypeMap(message.getBusinessId().getBusinessType());
        if (ObjectUtils.isNotEmpty(businessTypeEnum)) {
            BusinessEventMessage processCompletedMessage = new BusinessEventMessage();
            processCompletedMessage.setData(JSONObject.toJSONString(message));
            processCompletedMessage.setBusinessEventType(message.getBusinessId().getBusinessType());
            processCompletedMessage.setTopic("FLOW_EVENT_START");
            processCompletedMessage.setTimestamp(DateUtils.toTimestamp(message.getStartTime()));
            RocketMQContextHolder.send(BusinessEventMessage.TOPIC, processCompletedMessage);
        }
    }
}
