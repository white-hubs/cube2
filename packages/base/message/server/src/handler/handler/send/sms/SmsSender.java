package com.guhecloud.cube.base.message2.server.handler.handler.send.sms;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.handler.dto.MessageSendResponse;
import com.guhecloud.cube.base.message2.server.handler.handler.send.BaseMsgSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.CodeSender;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerHolder;
import com.guhecloud.cube.base.message2.server.handler.handler.send.ManufacturerSender;
import com.guhecloud.cube.base.message2.server.sat.enumeration.MsgTemplateTypeEnum;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmsSender extends BaseMsgSender {
    @Autowired
    private ManufacturerHolder manufacturerHolder;

    public SmsSender() {
        this.channelCode = ChannelEnum.CHANNEL_SMS;
    }

    @Override
    public MessageSendResponse doSend(MessageInfo messageInfo) {
        MsgTemplateTypeEnum tempType = messageInfo.getTempType();
        if (MsgTemplateTypeEnum.VERIFY_CODE.equals(tempType)) {
            ManufacturerSender manufacturerSender = manufacturerHolder.getHandler(
                    messageInfo.getChannelCode().name() + "-" + messageInfo.getManufacturerCode().name());
            CodeSender codeSender = (CodeSender) manufacturerSender;
            return codeSender.sendCode(messageInfo);

        }
        return this.manufacturerSender.send(messageInfo);
    }

}
