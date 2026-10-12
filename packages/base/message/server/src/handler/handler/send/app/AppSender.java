package com.guhecloud.cube.base.message2.server.handler.handler.send.app;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppSender extends BaseMsgSender {

    public AppSender() {
        this.channelCode = ChannelEnum.CHANNEL_APP;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        return this.manufacturerSender.send(messageInfo);
    }

    @Override
    public String verification(MessageInfo messageInfo) {
        return StringUtils.EMPTY_STRING;
    }
}
