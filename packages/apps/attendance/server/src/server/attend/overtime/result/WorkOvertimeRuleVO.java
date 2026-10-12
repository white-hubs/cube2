package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.result;

import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.WorkOvertimeRuleDetailVO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/11
 * @description 加班规则
 */
@Data
public class WorkOvertimeRuleVO {
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 加班上限周期(每天、每周、每月)
     */
    private String upperLimitCycle;

    /**
     * 加班上限累计时长(小时)
     */
    private Integer upperLimitDuration;
    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
    /**
     * 细则列表
     */
    private List<WorkOvertimeRuleDetailVO> ruleDetails;
    /**
     * 规则内容
     */
    private List<String> content;

}