package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

/**
 * 考勤组的假期规则
 */
@Data
public class AttGroupHolidayRuleResult {
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
    private Integer minAskUnit;
    /**
     * 一天折算小时
     */
    private Integer dayOfHour;
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
     * 发放方式（AttHolidayGrantWayEnum 每年发放一次
     */
    private String grantWay;
    /**
     * 发放日期（入职日发放，每年1月1发放
     */
    private String grantDate;
    /**
     * 额度类型（固定额度/按社会工龄/按公司工龄
     */
    private String quotaType;
    /**
     * 固定额度数量
     */
    private Integer fixedNumber;
    /**
     * 固定额度单位
     */
    private String fixedUnit;
    /**
     * 额度配置(json
     */
    private String quota;

    // 长期有效
    private Boolean longTerm;
    /**
     * 发放日起 多少自然年有效
     */
    private Integer natureYear;
    /**
     * 发放日起 多少个单位时间有效
     */
    private Integer validityNumber;
    /**
     * 有效期单位
     */
    private String validityUnit;
    /**
     * 有效期（json
     */
    private String validity;
}
