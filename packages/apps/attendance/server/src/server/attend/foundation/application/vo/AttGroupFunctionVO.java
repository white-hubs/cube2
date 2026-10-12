package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.vo;

import com.mzt.logapi.starter.annotation.DiffLogField;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  13:34
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttGroupFunctionVO {
    private String id;
    /**
     * 考勤组名称
     */
    @DiffLogField(name = "考勤组名称")
    private String name;
    /**
     * 打卡规则id
     */
    private Long clockRuleId;
    @DiffLogField(name = "打卡规则")
    private String clockRuleName;

    /**
     * 排班规则id
     */
    private Long schedulingRuleId;
    @DiffLogField(name = "排班规则")
    private String schedulingRuleName;
    /**
     * 补卡规则ids
     */
    private String cardRuleIds;
    @DiffLogField(name = "补卡规则")
    private String cardRuleName;
    /**
     * 加班规则id
     */
    private Long overtimeRuleId;
    @DiffLogField(name = "加班规则")
    private String overtimeRuleName;
    /**
     * 请假规则 ids
     */
    private String holidayRuleIds;
    @DiffLogField(name = "请假规则")
    private String holidayRuleName;
    /**
     * 离岗规则id
     */
    private Long leavePostRuleId;
    @DiffLogField(name = "离岗规则")
    private String leavePostRuleName;
    /**
     * 出差规则id;
     */
    private Long awayBusinessRuleId;
    @DiffLogField(name = "出差规则")
    private String awayBusinessRuleName;
    /**
     * 考勤组范围(所有人，部分人。枚举AttGroupRangesEnum）
     */
    @DiffLogField(name = "考勤组范围", function = "GROUP_RANGES")
    private String ranges;
    /**
     * 考勤组使用班次ids
     */
    private String groupClassIds;
    @DiffLogField(name = "考勤组班次")
    private String groupClassName;
    @DiffLogField(name = "适用人员")
    private String groupStaffList;
}
