package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceStatusUpdateMsg;

@FunctionalInterface
public interface DeviceStatusHandler extends MsgHandler{
    void handle(DeviceStatusUpdateMsg msg);
}
