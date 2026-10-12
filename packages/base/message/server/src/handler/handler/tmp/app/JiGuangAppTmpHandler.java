package com.guhecloud.cube.base.message2.server.handler.handler.tmp.app;

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
public class JiGuangAppTmpHandler extends BaseMsgChannelManufacturerTmpHandler {

    protected JiGuangAppTmpHandler() {
        super(ChannelEnum.CHANNEL_APP, ManufacturerCodeEnum.JI_GUANG);
    }

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        return this.getTmpVO(increaseMessageTmp.getTmpRule(), increaseMessageTmp.getTmpConfig());
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        return this.getTmpVO(changeMessageTmp.getTmpRule(), changeMessageTmp.getTmpConfig());
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        throw new UnsupportedOperationException();
    }
}
