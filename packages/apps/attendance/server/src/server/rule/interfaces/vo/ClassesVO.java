package com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/10
 * @description 班次管理
 */
@Data
public class ClassesVO {

    private Long id;

    /**
     * 汇总名称
     */
    private String summaryName;

    /**
     * 名称
     */
    @DiffLogField(name = "班次名称")
    private String name;

    /**
     * 上班时间
     */
    private String startWorkTime;

    /**
     * 上班时间前
     */
    private String beforeStartWorkTime;

    /**
     * 上班时间后
     */
    private String afterStartWorkTime;

    /**
     * 下班时间
     */
    private String endWorkTime;

    /**
     * 下班时间前
     */
    private String beforeEndWorkTime;

    /**
     * 下班时间后
     */
    private String afterEndWorkTime;

    /**
     * 休息开始时间
     */
    private String restStartTime;

    /**
     * 休息结束时间
     */
    private String restEndTime;

    /**
     * 标识
     */
    private String mark;

    /**
     * 下班时间是否是次日
     */
    private Boolean endTimeIsMorrow;

    /**
     * 建议应用范围(部门id)
     */
    private String suitOrganizationIds;

    /**
     * 应用范围名称
     */
    private String organizationNames;

}