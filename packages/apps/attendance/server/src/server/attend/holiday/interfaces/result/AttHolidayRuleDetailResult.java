package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.result;

import lombok.Data;

/**
 * 假期规则详情返回对象
 */
@Data
public class AttHolidayRuleDetailResult {
    private Long id;
    /**
     * 假期名称
     */
    private String name;

    /*
     * 假期申请规则
     */
    /**
     * 最小请假单位(低于此不能请假
     */
    private int minAskUnit;
    /**
     * 是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤
     */
    private Boolean intoAbsence;
    /**
     * 假期类型(法定类型)AttLegalHolidayEnum
     */
    private String holidayType;
    /**
     * 一天折算小时
     */
    private int dayOfHour;
    /**
     * 请假时间计算方式（工作日/自然日
     */
    private String calculationWay;

    /*
     * 假期发放规则
     */

    /**
     * 不限额(true表示不限额
     */
    private Boolean notLimit;

    /**
     * 发放方式（每年发放
     */
    private String grantWay;
    /**
     * 发放时间（入职日发放，每年1月1发放
     */
    private String grantDate;
    /**
     * 额度配置(json
     */
    private String quota;

    // 长期有效
    /**
     * 有效期（json
     */
    private String validity;
}
