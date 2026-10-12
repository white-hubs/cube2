package com.guhecloud.cube.base.message2.server.base.enumeration;

import lombok.Getter;

@Getter
public enum MsgSendStatusEnum {

    /**
     * 发送中
     */
    SENDING,

    /**
     * 发送失败
     */
    SEND_FAIL,

    /**
     * 发送成功
     */
    SEND_SUCCESSFULLY
}
