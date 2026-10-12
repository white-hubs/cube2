package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.cube.base.message2.server.initialization.event.IncreaseSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgChannelModifyMessage;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

/**
 * 租户通道配置修改消息
 *
 * @author lanchun
 * @date 2023/7/3 14:49
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "sat_msg_channel_change", topic = MsgChannelModifyMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgChannelModifyConsumer implements RocketMQListener<MsgChannelModifyMessage> {

    private final SatMsgTmpService satMsgTmpService;

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void onMessage(MsgChannelModifyMessage message) {
        List<SatMsgTmp> satMsgTmpList = this.satMsgTmpService.listSatMsgTmp(message.getChannelCode());
        if (!CollectionUtils.isEmpty(satMsgTmpList)) {
            //发送模板创建事件
            this.applicationEventPublisher.publishEvent(new IncreaseSatMsgTmpEvent(this, satMsgTmpList));
        }
    }
}
