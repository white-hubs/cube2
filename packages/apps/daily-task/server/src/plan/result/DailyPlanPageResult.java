package com.guhecloud.cube.biz.daily.task.plan.result;

import com.guhecloud.cube.biz.daily.task.commons.dto.DailySuperintendentDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 计划返回对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyPlanPageResult {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;
    private String planName;
    /**
     * 任务类型
     */
    private Long taskCategoryId;
    private String taskCategoryName;
    /**
     * 计划状态 DailyPlanStatusEnum
     */
    private String status;
    /**
     * 启用状态
     */
    private Boolean enable;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    /**
     * 执行次数
     */
    private Integer executionCount;
    /**
     * 紧急程度
     */
    private Long urgencyId;
    private String urgencyName;
    /**
     * 负责人信息
     */
    private List<DailySuperintendentDTO> superintendentList;
    /**
     * 对象数量
     */
    private Integer targetCount;
    private LocalDateTime createTime;
    /**
     * 创建人名称
     */
    private String createByName;
}
