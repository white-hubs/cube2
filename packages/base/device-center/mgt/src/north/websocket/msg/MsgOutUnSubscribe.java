package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MsgOutUnSubscribe extends MsgOut {
    @JSONField(serialize = false)
    private boolean result;
}
