package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.TenantAddApplicationMessage;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "sat_msg_tmp_init", topic = TenantAddApplicationMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgTmpInitConsumer implements RocketMQListener<TenantAddApplicationMessage> {

    private final SatMsgTmpService satMsgTmpService;

    @Override
    public void onMessage(TenantAddApplicationMessage message) {
        this.satMsgTmpService.refreshAppTemplate(message.getApplicationCodes(), message.getTenant());
    }
}
