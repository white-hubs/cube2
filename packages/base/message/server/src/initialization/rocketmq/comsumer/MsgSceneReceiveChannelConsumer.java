package com.guhecloud.cube.base.message2.server.initialization.rocketmq.comsumer;

import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgChannelModifyMessage;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceive;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgSceneReceiveChannel;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgSceneReceiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = "sat_msg_scene_receive", topic = MsgChannelModifyMessage.TOPIC, selectorType = SelectorType.TAG)
public class MsgSceneReceiveChannelConsumer implements RocketMQListener<MsgChannelModifyMessage> {

    private final SatMsgSceneReceiveChannelService satMsgSceneReceiveChannelService;

    private final SatMsgSceneReceiveService satMsgSceneReceiveService;

    @Override
    public void onMessage(MsgChannelModifyMessage message) {
        Long channelReceiveCount = this.satMsgSceneReceiveChannelService.getCountByChannel(message.getChannelCode());
        if (channelReceiveCount > 0) {
            //已初始化过
            return;
        }
        List<SatMsgSceneReceiveChannel> msgSceneReceiveChannels = new ArrayList<>();
        List<SatMsgSceneReceive> list = satMsgSceneReceiveService.list();
        for (SatMsgSceneReceive satMsgSceneReceive : list) {
            SatMsgSceneReceiveChannel satMsgSceneReceiveChannel = new SatMsgSceneReceiveChannel();
            satMsgSceneReceiveChannel.setSatMsgSceneReceiveId(satMsgSceneReceive.getId());
            satMsgSceneReceiveChannel.setChannelCode(message.getChannelCode());
            msgSceneReceiveChannels.add(satMsgSceneReceiveChannel);
        }
        if (!CollectionUtils.isEmpty(msgSceneReceiveChannels)) {
            this.satMsgSceneReceiveChannelService.saveBatch(msgSceneReceiveChannels);
        }
    }
}
