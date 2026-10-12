package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AttGroupOvertimeRuleResult {
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 计算单位(半小时、小时、天)
     */
    private String units;

    /**
     * 日折算时长
     */
    private Integer dayConvertTime;

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
     * 规则内容
     */
    private List<String> content;
}
