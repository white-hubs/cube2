package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class AttClockSchedulingClassesDTO extends BasicEnhancedTenantEntity<Long> {

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
     * 班次日期
     */
    private LocalDateTime classDate;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班打卡开始时间
     */
    private String startClockBeforeTime;

    /**
     * 上班打卡结束时间
     */
    private String startClockAfterTime;

    /**
     * 下班时间
     */
    private String endWorkTime;

    /**
     * 下班打卡开始时间
     */
    private String endClockBeforeTime;

    /**
     * 下班打卡结束时间
     */
    private String endClockAfterTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

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
     * 班次版本
     */
    private Integer version;

    /**
     * 建议应用范围(部门id)
     */
    private String deptIds;
}
