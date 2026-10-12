package com.guhecloud.cube.biz.daily.task.enums;

import lombok.Getter;

/**
 * 执行方式
 */
@Getter
public enum DailyExecuteWayEnum {
    NFC("NFC"),
    BLUETOOTH("蓝牙"),
    QRCODE("二维码"),
    ;
    private final String value;

    DailyExecuteWayEnum(String value) {
        this.value = value;
    }
}
