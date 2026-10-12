package com.guhecloud.cube.base.message2.server.handler.handler.tmp;

import com.guhecloud.cube.base.message2.server.handler.pending.tmp.ChangeMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.DeleteMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;

public interface MsgChannelManufacturerTmpHandler {

    TmpVO increaseTmp(IncreaseMessageTmp increaseMessageTmp);

    TmpVO changeTmp(ChangeMessageTmp changeMessageTmp);

    void removeTmp(DeleteMessageTmp deleteMessageTmp);
}
