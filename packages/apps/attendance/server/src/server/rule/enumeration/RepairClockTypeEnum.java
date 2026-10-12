package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

/**
 * 补卡类型枚举
 *
 * @author appet
 * @date 2022/11/28
 */
@Getter
public enum RepairClockTypeEnum {
    START("上班卡"),
    END("下班卡");

    private final String value;

    RepairClockTypeEnum(String value) {
        this.value = value;
    }

}
