package com.guhecloud.cube.base.message.sdk;

import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message2.client.SatMsgSceneReceiveClient;
import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MessageSceneClient {

    @Autowired
    private SatMsgSceneReceiveClient satMsgSceneReceiveClient;

    public SatMsgSceneReceiveDTO getSatMsgSceneReceive(String sceneCode) {
        return this.satMsgSceneReceiveClient.getSatMsgSceneReceiveByCode(sceneCode);
    }

    public List<MessageChannelEnum> getMsgSceneReceiveChannels(String sceneCode) {
        SatMsgSceneReceiveDTO satMsgSceneReceiveDTO = this.getSatMsgSceneReceive(sceneCode);
        if (Objects.isNull(satMsgSceneReceiveDTO)) {
            return Collections.emptyList();
        }
        return satMsgSceneReceiveDTO.getChannelCodes().stream()
                .map(ChannelEnum -> MessageChannelEnum.valueOf(ChannelEnum.name()))
                .collect(Collectors.toList());
    }
}
