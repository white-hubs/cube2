package com.guhecloud.cube.iot.mgt.core.data.lang.cmd;

import lombok.Getter;

@Getter
public enum DownCmd {
    DEV_PROP_SET("DEV_PROP_SET", "设备属性设置"),
    DEV_PROP_RESET("DEV_PROP_RESET", "设备属性重置"),
    DEV_EVENT_RESET("DEV_EVENT_RESET", "设备事件重置"),
    DEV_INFO_SYNC("DEV_INFO_SYNC", "设备信息同步"),
    DEV_LINKAGE_TASK_PROP_DOWN("DEV_LINKAGE_TASK_PROP_DOWN", "联动任务属性下发"),
    METHOD_INVOKE("METHOD_INVOKE", "设备方法调用"),
    ;

    private final String code;
    private final String name;

    DownCmd(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
