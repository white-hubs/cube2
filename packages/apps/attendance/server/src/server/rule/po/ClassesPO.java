package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/10
 * @description 班次管理
 */
@Data
@TableName("classes")
public class ClassesPO extends BasicEnhancedTenantEntity<Long> {
    @DiffLogField(name = "班次id")
    private Long id;

    /**
     * 名称
     */
    @DiffLogField(name = "班次名称")
    private String name;

    /**
     * 上班时间
     */
    @DiffLogField(name = "上班时间")
    private String startWorkTime;

    /**
     * 上班时间前
     */
    @DiffLogField(name = "上班时间前")
    private String beforeStartWorkTime;

    /**
     * 上班时间后
     */
    @DiffLogField(name = "上班时间后")
    private String afterStartWorkTime;

    /**
     * 下班时间
     */
    @DiffLogField(name = "下班时间")
    private String endWorkTime;

    /**
     * 下班时间前
     */
    @DiffLogField(name = "下班时间前")
    private String beforeEndWorkTime;

    /**
     * 下班时间后
     */
    @DiffLogField(name = "下班时间后")
    private String afterEndWorkTime;

    /**
     * 休息开始时间
     */
    @DiffLogField(name = "休息开始时间")
    private String restStartTime;

    /**
     * 休息结束时间
     */
    @DiffLogField(name = "休息结束时间")
    private String restEndTime;

    /**
     * 标志
     */
    @DiffLogField(name = "标志")
    private String mark;

    /**
     * 下班时间是否是次日
     */
    @DiffLogField(name = "下班时间是否是次日", function = "BOOLEAN")
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String suitOrganizationIds;
}