package com.guhecloud.cube.base.message2.server.base.rocketmq.producer;

import com.guhecloud.cube.base.message2.server.base.entity.MsgTmp;
import com.guhecloud.cube.base.message2.server.base.service.MsgTmpService;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgTmpAddMessage;
import com.guhecloud.platform.rocketmq.producer.RocketMQTransactionChecker;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgTmpAddTransactionChecker implements RocketMQTransactionChecker<MsgTmpAddMessage> {

    private final MsgTmpService msgTmpService;

    @Override
    public RocketMQLocalTransactionState check(Message<?> message, MsgTmpAddMessage msgTmpAddMessage) {
        MsgTmp msgTmp = this.msgTmpService.getById(msgTmpAddMessage.getId());
        return Objects.nonNull(msgTmp) ? RocketMQLocalTransactionState.COMMIT : RocketMQLocalTransactionState.ROLLBACK;
    }

    @Override
    public String getTopic() {
        return MsgTmpAddMessage.TOPIC;
    }

    @Override
    public Class<MsgTmpAddMessage> getPayloadClass() {
        return MsgTmpAddMessage.class;
    }
}
