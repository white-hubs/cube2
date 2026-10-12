package com.guhecloud.cube.srv.gen.disinfection.server.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 定时任务模式枚举
 */
@AllArgsConstructor
@Getter
public enum ScheduleModeEnum {
    /** 每天 */
    DAILY("每天", 0),
    /** 每周 */
    WEEKLY("每周", 1);

    private final String name;
    private final Integer value;
}
