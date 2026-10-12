package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.timing.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 定时规则
 *
 * @author lanchun
 * @date 2022/6/29 16:13
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RuleTiming extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 规则主键
     */
    private Long ruleId;

    /**
     * 是否循环
     */
    private Boolean looped;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 定时任务主键
     */
    private String jobId;
}
