package com.guhecloud.cube.base.alarm.server.flow.domain.enumeration;

import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/2 10:16
 * @Description: 文件场景code定义
 */

@Getter
public enum AlarmFileSceneCodeEnum {
    ALARM_RECORD_FOLLOW_UP("ALARM:RECORD_FOLLOW_UP", "报警记录跟进"),
    ALARM_RECORD_HANDLER("ALARM:RECORD_HANDLER", "报警记录处理"),
    ALARM_RECORD_SUSPEND("ALARM:RECORD_SUSPEND", "报警记录挂起"),
    ALARM_LEVEL_PLAY_CONFIG("ALARM:LEVEL_PLAY_CONFIG", "报警级别播放配置"),
    ;
    private final String code;
    private final String desc;

    AlarmFileSceneCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
