package com.guhecloud.cube.srv.gen.attendance.management.server.rule.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @author bq
 * @date 2022/11/10
 * @description 加班规则
 */
@Data
@TableName("work_overtime_rule")
public class WorkOvertimeRulePO extends BasicEnhancedTenantEntity<Long> {

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

}