package com.guhecloud.cube.base.message2.server.handler.handler.tmp.email;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.BaseMsgChannelManufacturerTmpHandler;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SmtpEmailTmpHandler extends BaseMsgChannelManufacturerTmpHandler {

    protected SmtpEmailTmpHandler() {
        super(ChannelEnum.CHANNEL_EMAIL, ManufacturerCodeEnum.SMTP);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        return null;
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        return null;
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {

    }
}
