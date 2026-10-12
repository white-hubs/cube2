package com.guhecloud.cube.base.message2.server.handler.handler.tmp.sms;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.BaseMsgChannelManufacturerTmpHandler;

public abstract class BaseSmsTmpHandler extends BaseMsgChannelManufacturerTmpHandler {

    protected BaseSmsTmpHandler(ChannelEnum channelCode, ManufacturerCodeEnum manufacturerCode) {
        super(channelCode, manufacturerCode);
    }

    protected abstract String convertTemplateContent(String content);
}
