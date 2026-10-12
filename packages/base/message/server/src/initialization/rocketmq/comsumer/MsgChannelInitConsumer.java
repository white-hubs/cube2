package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgChannelModifyMessage;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.TenantInitMessage;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "sat_msg_channel_init", topic = TenantInitMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgChannelInitConsumer implements RocketMQListener<TenantInitMessage> {

    private final SatMsgChannelService satMsgChannelService;

    @Override
    public void onMessage(TenantInitMessage message) {
        SessionContextHolder.requestTenant(message.getTenant());
        SatMsgChannelModifyParam param = new SatMsgChannelModifyParam();
        param.setChannelCode(ChannelEnum.CHANNEL_SITE);
        param.setManufacturerCode(ManufacturerCodeEnum.GUHE_YUN);
        param.setEnabled(true);
        this.satMsgChannelService.save(param);
        //发送事务消息,新增成功后-初始化模板
        RocketMQContextHolder.sendTransactionalMessage(MsgChannelModifyMessage.TOPIC,
                                                       MsgChannelModifyMessage.builder()
                                                               .channelCode(ChannelEnum.CHANNEL_SITE)
                                                               .manufacturerCode(param.getManufacturerCode().name())
                                                               .config(param.getConfig())
                                                               .build(), () -> RocketMQLocalTransactionState.COMMIT);
    }
}
