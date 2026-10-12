package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgOutHeartBeat extends MsgOut {

    private long ts;

    public static MsgOutHeartBeat build(long ts) {
        MsgOutHeartBeat msgOutHeartBeat = new MsgOutHeartBeat();
        msgOutHeartBeat.setMsgType(MsgTypeEnum.HEART_BEAT);
        msgOutHeartBeat.setTs(ts);
        return msgOutHeartBeat;
    }
}
