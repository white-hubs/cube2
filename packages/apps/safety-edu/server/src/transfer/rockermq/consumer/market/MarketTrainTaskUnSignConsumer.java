package com.guhecloud.cube.srv.gen.safety.education.server.transfer.rockermq.consumer.market;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.message.CultivateUnSignMessage;
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
 * 市场培训任务未签到消息消费
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = TransferTopicConstant.MARKET_PUSH_TRAIN_UN_SIGN_GROUP, topic = CultivateUnSignMessage.TOPIC)
@ConditionalOnBean(name = "transferRocketMQProperties")
public class MarketTrainTaskUnSignConsumer implements RocketMQListener<CultivateUnSignMessage> {

    private final DataTransferService dataTransferService;

    @Override
    public void onMessage(CultivateUnSignMessage cultivateUnSignMessage) {
        org.springframework.messaging.Message<String> stringMessage = MessageBuilder
                .withPayload(JsonUtils.serialize(cultivateUnSignMessage)).build();
        //接受消息转发给集团
        dataTransferService.marketMessagePushGroup(cultivateUnSignMessage.getTask().getId().toString(), cultivateUnSignMessage.getTask()
                                                           .getSourceCode(),
                                                   new Message(TransferTopicConstant.GROUP_RECEIVE_TRAIN_UN_SIGN_TOP, "*",
                                                               JsonUtils.serialize(stringMessage).getBytes()));
    }
}
