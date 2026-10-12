package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 值班
 */
@Data
@Builder
@TableName("att_duty")
@EqualsAndHashCode(callSuper = true)
public class AttDutyPO extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 值班月份
     */
    private String dutyMonth;
    /**
     * 值班日期
     */
    private LocalDateTime dutyDate;
    /**
     * 值班班次id
     */
    private Long classesId;
    /**
     * 值班班次名称
     */
    private String classesName;
    /**
     * 值班班次别名
     */
    private String classesNickName;
    /**
     * 班次类型
     */
    private String classesType;
    /**
     * 跨天班次
     */
    private Boolean classesEndTimeIsMorrow;
    /**
     * 上班开始时间
     */
    private LocalDateTime classesRealStartTime;
    /**
     * 下班打卡时间
     */
    private LocalDateTime classesRealEndTime;
    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 员工名称
     */
    private String staffName;
    /**
     * 员工部门id
     */
    private Long staffDeptId;
    /**
     * 员工部门名称
     */
    private String staffDeptName;
}
