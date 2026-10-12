package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgInHeartBeat extends MsgIn {
    private long ts;
}
