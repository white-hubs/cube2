package com.guhecloud.cube.base.message2.server.handler.handler.tmp;

import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import org.springframework.stereotype.Component;

@Component
public class DefaultMsgChannelManufacturerTmpHandler implements MsgChannelManufacturerTmpHandler {

    @Override
    public TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp) {
        //默认实现,不做任何处理
        return null;
    }

    @Override
    public TmpVO changeTmp(ChangeMessageTmp changeMessageTmp) {
        //默认实现,不做任何处理
        return null;
    }

    @Override
    public void removeTmp(DeleteMessageTmp deleteMessageTmp) {
        //默认实现,不做任何处理
    }
}
