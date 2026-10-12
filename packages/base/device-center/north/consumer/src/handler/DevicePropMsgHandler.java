package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.LivePropertyUpdateMsg;

@FunctionalInterface
public interface DevicePropMsgHandler extends MsgHandler {

    void handle(LivePropertyUpdateMsg msg);
}
