package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.market;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamMissMessage;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.constant.TransferTopicConstant;
import com.guhecloud.cube.srv.gen.safety.education.server.transfer.service.DataTransferService;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * 市场考试任务缺考消息消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.MARKET_PUSH_EXAM_MISS_GROUP, topic = UserExamMissMessage.TOPIC)
@ConditionalOnBean(name = "transferRocketMQProperties")
public class MarketExamTaskMissConsumer implements RocketMQListener<UserExamMissMessage> {

    private final DataTransferService dataTransferService;

    @Override
    public void onMessage(UserExamMissMessage userExamMissMessage) {
        org.springframework.messaging.Message<String> stringMessage = MessageBuilder
                .withPayload(JsonUtils.serialize(userExamMissMessage)).build();
        //接受消息转发给集团
        dataTransferService.marketMessagePushGroup(userExamMissMessage.getId().toString(), userExamMissMessage.getSourceCode(),
                                                   new Message(TransferTopicConstant.GROUP_RECEIVE_EXAM_MISS_TOP, "*",
                                                               JsonUtils.serialize(stringMessage).getBytes()));
    }
}
