package com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/14
 * @description 考勤组加班规则
 */
@Data
@TableName("attendance_group_work_overtime_rule")
public class SubmitAttendanceGroupWorkOvertimeRuleCommand {

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

    private String config;

    /**
     * 规则细则
     */
    private List<SubmitWorkOvertimeRuleCommand.Detail> detailVOS;

    public void setDetailVOS(List<SubmitWorkOvertimeRuleCommand.Detail> detailVOS) {
        this.detailVOS = detailVOS;
        if (!CollectionUtils.isEmpty(detailVOS)) {
            this.config = JSONObject.toJSONString(detailVOS);
        }
    }

}