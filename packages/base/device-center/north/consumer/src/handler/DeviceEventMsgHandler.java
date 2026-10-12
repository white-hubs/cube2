package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventTriggerMsg;

@FunctionalInterface
public interface DeviceEventMsgHandler extends MsgHandler {
    void handle(DeviceEventTriggerMsg msg);
}
