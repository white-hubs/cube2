package com.guhecloud.cube.iot.mgt.core.data.lang;

import com.guhecloud.cube.iot.mgt.core.data.exception.IllegalDeviceStatus;

/**
 * 设备通信状态<br/>
 * 标识物联平台与设备之间的通信状态,由任意值上报或心跳触达，或由南向接口直接推送标记。
 */
public enum CommunicationStatus {

    NORMAL("正常", 1),

    LOST("掉线", 2);

    public final int value;
    public final String name;

    CommunicationStatus(String name, int value) {
        this.value = value;
        this.name = name;
    }

    public static CommunicationStatus valueOf(int value) {
        switch (value) {
            case 1:
                return NORMAL;
            case 2:
                return LOST;
            default:
                throw new IllegalDeviceStatus("非法的设备通信状态值:" + value);
        }
    }
}
