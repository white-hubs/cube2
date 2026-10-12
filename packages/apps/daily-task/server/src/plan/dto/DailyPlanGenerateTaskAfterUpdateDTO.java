package com.guhecloud.cube.biz.daily.task.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 计划生成任务后计划修改对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanGenerateTaskAfterUpdateDTO {
    private Long id;
    /**
     * 上次执行时间
     */
    private LocalDateTime lastTime;
    /**
     * 下次执行时间
     */
    private LocalDateTime nextTime;
    /**
     * 执行次数
     */
    private Integer executionCount;
    private LocalDateTime updateTime;
}
