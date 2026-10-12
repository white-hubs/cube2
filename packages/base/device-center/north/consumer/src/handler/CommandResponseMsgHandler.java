package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceCommandInfoMsg;

@FunctionalInterface
public interface CommandResponseMsgHandler extends MsgHandler {
    void handle(DeviceCommandInfoMsg msg);
}
