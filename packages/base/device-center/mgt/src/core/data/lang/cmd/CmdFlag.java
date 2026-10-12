package com.guhecloud.cube.iot.mgt.core.data.lang.cmd;

import lombok.Getter;

/**
 * 命令标志位
 */
@Getter
public enum CmdFlag {
    UN_COMPLETED(0, "未完成"),
    COMPLETED(1, "完成"),
    CANCELED(4, "取消");

    private final int code;
    private final String description;

    CmdFlag(int code, String description) {
        this.code = code;
        this.description = description;
    }

}
