package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

/**
 * 排班规则的班次返回对象
 */
@Data
public class AttSchedulingRuleClassesResult {
    /**
     * 班次id
     */
    private Long id;
    /**
     * 班次名称
     */
    private String name;
    /**
     * 班次别称
     */
    private String nickName;
    /**
     * 班次类型
     */
    private String classesType;
    /**
     * 星期(枚举)
     */
    private String week;
}
