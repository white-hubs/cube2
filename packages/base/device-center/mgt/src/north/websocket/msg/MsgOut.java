package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import lombok.Data;

@Data
public abstract class MsgOut {
    protected MsgTypeEnum msgType;
    protected String content;
}
