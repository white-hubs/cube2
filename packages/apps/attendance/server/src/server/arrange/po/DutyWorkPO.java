package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author bq
 * @date 2022/11/16
 * @description 值班管理
 */
@Data
@TableName("duty_work")
public class DutyWorkPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 员工ID
     */
    private Long staffId;

    /**
     * 员工姓名
     */
    private String staffName;

    /**
     * 员工电话
     */
    private String staffMobile;

    /**
     * 员工部门
     */
    private Long staffOrganizationId;

    /**
     * 班次IDS
     */
    @DiffLogField(name = "班次", function = "DUTY_WORK_CLASSES")
    private String dutyWorkClassesIds;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 月份
     */
    private String month;

    /**
     * 周几
     */
    private Integer week;

    /**
     * 日期
     */
    private LocalDateTime date;

    /**
     * 是否休息
     */
    private Boolean isRest;

    /**
     * 是否节日
     */
    private Boolean isHoliday;

}