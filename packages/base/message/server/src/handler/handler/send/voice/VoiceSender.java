package com.guhecloud.cube.base.message2.server.handler.handler.send.voice;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class VoiceSender extends BaseMsgSender {

    public VoiceSender() {
        this.channelCode = ChannelEnum.CHANNEL_VOICE;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        return this.manufacturerSender.send(messageInfo);
    }
}
