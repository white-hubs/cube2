package com.guhecloud.cube.iot.north.bridge.consumer.handler;

import com.guhecloud.cube.iot.north.bridge.consumer.vo.RuleTriggerMsg;

@FunctionalInterface
public interface RuleEventMsgHandler extends MsgHandler{
    void handle(RuleTriggerMsg msg);
}
