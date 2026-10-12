package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgDictionaryConfigMessage;
import com.guhecloud.cube.base.message2.server.sat.controller.SatMsgChannelController;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelModifyParam;
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
@RocketMQMessageListener(consumerGroup = "sat_msg_channel", topic = MsgDictionaryConfigMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgDictionaryModifyConsumer implements RocketMQListener<MsgDictionaryConfigMessage> {

    private static final String WECHAT_PUBLIC_ACCOUNT_KEY = "WX_OFFICIAL_ACCOUNT";

    private final SatMsgChannelController satMsgChannelController;

    @Override
    public void onMessage(MsgDictionaryConfigMessage message) {
        if (!WECHAT_PUBLIC_ACCOUNT_KEY.equals(message.getCode())) {
            return;
        }
        SatMsgChannelModifyParam satMsgChannelModifyParam = new SatMsgChannelModifyParam();
        satMsgChannelModifyParam.setChannelCode(ChannelEnum.CHANNEL_WE_PUBLIC_SERVICE_ACCOUNT);
        satMsgChannelModifyParam.setManufacturerCode(ManufacturerCodeEnum.WE_CHAT);
        satMsgChannelModifyParam.setEnabled(true);
        satMsgChannelModifyParam.setConfig(message.getVariable());
        satMsgChannelController.save(satMsgChannelModifyParam);
    }
}
