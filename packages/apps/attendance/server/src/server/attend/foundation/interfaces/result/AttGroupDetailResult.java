package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

import java.util.List;

/**
 * 考勤组详情返回对象
 */
@Data
public class AttGroupDetailResult {
    private Long id;
    /**
     * 考勤组名称
     */
    private String name;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    /**
     * 补卡规则ids
     */
    private String cardRuleIds;
    /**
     * 加班规则id
     */
    private Long overtimeRuleId;
    /**
     * 离岗规则id
     */
    private Long leavePostRuleId;
    /**
     * 出差规则id;
     */
    private Long awayBusinessRuleId;
    /**
     * 考勤组范围(所有人，部分人。枚举AttGroupRangesEnum）
     */
    private String ranges;
    /**
     * 考勤组人员集合
     */
    private List<AttGroupStaffResult> groupStaffList;
    /**
     * 考勤组使用班次集合
     */
    private List<AttGroupClassResult> groupClassList;
    /**
     * 加班规则
     */
    private AttGroupOvertimeRuleResult overtimeRule;
    /**
     * 补卡规则集合
     */
    private List<AttGroupCardRuleResult> cardRuleList;
    /**
     * 假期规则集合
     */
    private List<AttGroupHolidayRuleResult> holidayRuleList;
    /**
     * 离岗规则
     */
    private AttGroupLeavePostRuleResult leavePostRule;
    /**
     * 出差规则
     */
    private AttGroupAwayBusinessRuleResult awayBusinessRule;
}
