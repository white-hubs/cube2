package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

/**
 * 加班补偿枚举
 *
 * @author bq
 * @date 2022/11/16
 */
@Getter
public enum WorkOvertimeTypeEnum {
    /**
     * 工作日
     */
    WORK("工作日"),

    /**
     * 休息日
     */
    REST("休息日"),

    /**
     * 节假日
     */
    FESTIVAL("节假日");
    private final String value;

    WorkOvertimeTypeEnum(String value) {this.value = value;}

}
