package com.guhecloud.cube.iot.mgt.north.websocket.msg;

import java.util.Objects;

public enum MsgTypeEnum {
    /**
     * 心跳
     */
    HEART_BEAT,
    /**
     * 订阅
     */
    SUBSCRIBE,
    /**
     * 取消订阅
     */
    UNSUBSCRIBE,
    /**
     * 订阅推送
     */
    SUBSCRIBE_PUSH, WELCOME, BYE_BYE;

    public static MsgTypeEnum toMsgTypeEnum(String msgType) {
        for (MsgTypeEnum msgTypeEnum : MsgTypeEnum.values()) {
            if (Objects.equals(msgTypeEnum.name(), msgType)) {
                return msgTypeEnum;
            }
        }

        throw new IllegalArgumentException("msgType is illegal");
    }
}
