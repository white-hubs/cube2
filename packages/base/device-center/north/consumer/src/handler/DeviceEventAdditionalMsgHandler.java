package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.DeviceEventAdditionalMsg;

@FunctionalInterface
public interface DeviceEventAdditionalMsgHandler extends MsgHandler{
    void handle(DeviceEventAdditionalMsg msg);
}
