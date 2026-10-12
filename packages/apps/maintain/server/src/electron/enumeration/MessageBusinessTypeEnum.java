package com.guhecloud.cube.srv.gen.maintain.server.electron.enumeration;

import lombok.Getter;

/**
 * 消息通知业务类型
 */
@Getter
public enum MessageBusinessTypeEnum {
    MAINTAIN("日常巡检"),
    ELECTRON("电子巡更");

    private String remarks;

    MessageBusinessTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
