package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 排班班次
 */
@Data
public class AttSchedulingClassesDTO {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 别称
     */
    private String nickName;

    /**
     * 上班时间
     */
    private LocalDateTime startWorkTime;

    /**
     * 上班打卡开始时间
     */
    private LocalDateTime startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private LocalDateTime startClockAfterTime;

    /**
     * 下班时间
     */
    private LocalDateTime endWorkTime;

    /**
     * 下班打卡开始时间
     */
    private LocalDateTime endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    private LocalDateTime endClockAfterTime;

    /**
     * 休息开始时间
     */
    private LocalDateTime restStartTime;

    /**
     * 休息结束时间
     */
    private LocalDateTime restEndTime;

    /**
     * 班次类型
     * 枚举AttClassesTypeEnum，（如果有需求可拓展成班次类型模块）
     */
    private String classesType;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String deptIds;
    /**
     * 排班是否开始
     */
    private Boolean started;
}
