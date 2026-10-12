package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.market;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.infrastructure.mq.rocketmq.message.UserExamSubmittedMessage;
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
 * 市场考试任务提交完成消息消费
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.MARKET_PUSH_EXAM_GROUP, topic = UserExamSubmittedMessage.TOPIC)
@ConditionalOnBean(name = "transferRocketMQProperties")
public class MarketExamTaskFinishConsumer implements RocketMQListener<UserExamSubmittedMessage> {

    private final DataTransferService dataTransferService;

    /**
     * 监听市场人员考试完成的消息，使用集团的MQ信息推送给集团
     *
     * @param userExamSubmittedMessage
     */
    @Override
    public void onMessage(UserExamSubmittedMessage userExamSubmittedMessage) {
        org.springframework.messaging.Message<String> stringMessage = MessageBuilder
                .withPayload(JsonUtils.serialize(userExamSubmittedMessage)).build();
        //接受消息转发给集团
        dataTransferService.marketMessagePushGroup(userExamSubmittedMessage.getId().toString(), userExamSubmittedMessage.getSourceCode(),
                                                   new Message(TransferTopicConstant.GROUP_RECEIVE_EXAM_TOP, "*",
                                                               JsonUtils.serialize(stringMessage).getBytes()));
    }
}
