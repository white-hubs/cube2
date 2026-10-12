package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2023-12-25  19:08
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum WorkOvertimeTimeCheckEnum {
    UNLIMITED("无限制"),
    ADVANCE("加班前发起");
    private final String value;

    WorkOvertimeTimeCheckEnum(String value) {
        this.value = value;
    }
}
