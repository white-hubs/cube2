package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组加班规则
 */
@Data
@TableName("attendance_group_work_overtime_rule")
public class AttendanceGroupWorkOvertimeRulePO {

    private Long id;

    /**
     * 考勤组ID
     */
    private Long groupId;

    /**
     * 加班规则ID
     */
    private Long workOvertimeRuleId;

    /**
     * 冗余加班规则-名称
     */
    private String name;

    /**
     * 冗余加班规则-计算单位(半小时、小时、天)
     */
    private String units;

    /**
     * 冗余加班规则-日折算时长
     */
    private String dayConvertTime;

    /**
     * 冗余加班规则-加班上限周期(每天、每周、每月)
     */
    private String upperLimitCycle;

    /**
     * 冗余加班规则-加班上限累计时长(小时)
     */
    private Integer upperLimitDuration;

    /**
     * 加班规则细则配置json
     */
    private String config;

}