package com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration;

import lombok.Getter;

/**
 * 加班补偿方式枚举
 *
 * @author bq
 * @date 2023/03/14
 */
@Getter
public enum WorkOvertimeCompensateMethodEnum {
    /**
     * 系统自动计算
     */
    SYSTEM("系统自动计算"),

    /**
     * 人工
     */
    ARTIFICIAL("人工判定"),

    /**
     * 前端传值与定义不同,新加一个兼容历史
     */
    ARTIFICAL("人工判定");
    
    private final String value;

    WorkOvertimeCompensateMethodEnum(String value) {this.value = value;}
}
