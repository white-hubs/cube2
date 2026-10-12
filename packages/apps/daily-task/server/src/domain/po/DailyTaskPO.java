package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.commons.service.TaskCommonFields;
import com.guhecloud.cube.biz.daily.task.status.base.BaseState;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_task")
@EqualsAndHashCode(callSuper = true)
public class DailyTaskPO extends BasicEnhancedTenantTreeEntity<Long> implements TaskCommonFields, BaseState {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;

    /**
     * 任务类型id
     */
    private Long taskCategoryId;

    /**
     * 任务类型（普通、标准、对象）
     */
    private String taskType;

    /**
     * 任务名称
     */
    private String name;
    private Long planId;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 紧急程度id
     */
    private Long urgencyId;
    /**
     * 任务对象数
     */
    private Integer targetCount;

    /**
     * 任务进度
     */
    private BigDecimal progress;
    /**
     * 任务截至日期(任务的期限)
     */
    private LocalDateTime deadline;
    /**
     * 执行开始时间(人员开始执行任务的时间)
     */
    private LocalDateTime startTime;
    /**
     * 执行结束时间(任务完成的时间)
     */
    private LocalDateTime endTime;
    /**
     * 文件关联key
     * (不直接用id关联是因为用单独的key可以直接知道是否有文件,没有就不用去查询了)
     */
    private Long fileRefKey;
    /**
     * 任务有效期
     */
    private Integer taskDay;
    private Integer taskHour;
    private Integer taskMinute;

    /**
     * 处理方式
     */
    private String method;

    /**
     * 是否发放给在岗人员
     */
    private Boolean isOnDuty;

    /**
     * 完成进度
     */
    private Long requireProgress;

    /**
     * 描述
     */
    private String description;
    /**
     * 是否是连续任务
     */
    private Boolean continuity;
}
