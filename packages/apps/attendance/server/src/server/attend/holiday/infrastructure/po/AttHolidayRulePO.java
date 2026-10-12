package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 假期类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("att_holiday_rule")
@EqualsAndHashCode(callSuper = true)
public class AttHolidayRulePO extends BasicEnhancedTenantEntity<Long> {
    @DiffLogField(name = "规则id")
    private Long id;
    /**
     * 假期名称
     */
    @DiffLogField(name = "假期名称")
    private String name;
    /**
     * 最小请假单位(低于此不能请假
     */
    @DiffLogField(name = "最小请假单位")
    private Integer minAskUnit;
    /**
     * 是否计入缺勤(true请该假期算缺勤，false请该假期算正常出勤
     */
    @DiffLogField(name = "是否计入缺勤", function = "HOLIDAY_BOOLEAN")
    private Boolean intoAbsence;
    /**
     * 假期类型(法定类型)AttLegalHolidayEnum
     */
    @DiffLogField(name = "假期类型", function = "HOLIDAY_TYPE")
    private String holidayType;
    /**
     * 一天折算小时
     */
    @DiffLogField(name = "一天折算小时")
    private Integer dayOfHour;
    /**
     * 请假时间计算方式（AttAskLeaveCalcWayEnum（工作日/自然日
     */
    @DiffLogField(name = "请假时间计算方式", function = "HOLIDAY_CALC_WAY")
    private String calculationWay;
    /**
     * 不限额(true表示不限额
     */
    @DiffLogField(name = "假期额度", function = "HOLIDAY_NOTLIMIT")
    private Boolean notLimit;

    /**
     * 发放方式（AttHolidayGrantWayEnum 每年发放一次/加班计入余额
     */
    @DiffLogField(name = "发放方式", function = "HOLIDAY_GRANT_WAY")
    private String grantWay;
    /**
     * 发放日期（入职日发放，每年1月1发放
     */
    @DiffLogField(name = "发放日期")
    private String grantDate;
    /**
     * 额度类型（AttHolidayQuotaTypeEnum 固定额度/按社会工龄/按公司工龄
     */
    private String quotaType;
    /**
     * 固定额度数量
     */
    private Integer fixedNumber;
    /**
     * 固定额度单位（AttConstants
     */
    @DiffLogField(name = "固定额度单位", function = "HOLIDAY_FIXED_UNIT")
    private String fixedUnit;
    /**
     * 额度配置(json
     */
    @DiffLogField(name = "额度配置", function = "HOLIDAY_QUOTA_CONFIG")
    private String quota;
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
     * 有效期单位（AttConstants
     */
    private String validityUnit;
    /**
     * 有效期（json
     */
    @DiffLogField(name = "有效期", function = "HOLIDAY_VALIDITY")
    private String validity;
}
