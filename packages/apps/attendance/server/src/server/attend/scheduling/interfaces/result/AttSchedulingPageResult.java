package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * 排班分页返回对象
 */
@Data
public class AttSchedulingPageResult {

    /**
     * 员工id
     */
    private Long staffId;
    /**
     * 员工名称
     */
    private String staffName;
    /**
     * 考勤组id
     */
    private Long groupId;
    /**
     * 规则id
     */
    private Long ruleId;
    /**
     * 法定节假日是否休息
     */
    private Boolean legalHolidaysRest;
    /**
     * 排班方式
     */
    private String schedulingWays;
    /**
     * 规则的班次
     */
    private List<AttSchedulingRuleClassesResult> schedulingRuleClassesList;
    /**
     * 手动的排班
     */
    private List<AttSchedulingHandResult> schedulingHandList;

}
