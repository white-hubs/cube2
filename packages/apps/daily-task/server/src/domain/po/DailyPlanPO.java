package com.guhecloud.cube.biz.daily.task.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.daily.task.commons.service.TaskCommonFields;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 计划
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("daily_plan")
@EqualsAndHashCode(callSuper = true)
public class DailyPlanPO extends BasicEnhancedTenantEntity<Long> implements TaskCommonFields {
    private Long id;
    /**
     * 日常任务类型
     */
    private String dailyType;
    private String name;
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
     * 执行方式 DailyExecuteWayEnum
     * 多种以逗号隔开,空表示可以直接点击执行.
     */
    private String executeWay;
    /**
     * 表达式id
     */
    private Long cronId;
    /**
     * 指定类型 JobLoopTypeEnum
     */
    private String specifiedType;
    /**
     * 指定的日期(根据指定的类型 这里可能是 月/周/号 等)
     * 例如 specifiedDays的值为7时: specifiedType为MONTH(每月)这里就是7月; specifiedType为WEEK时这里就是周日
     */
    private String specifiedDays;
    /**
     * 指定的时间(这里只有HH:mm:ss)
     */
    private String specifiedTime;
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
    /**
     * 任务类型id
     */
    private Long taskCategoryId;
    /**
     * 紧急程度id
     */
    private Long urgencyId;
    /**
     * 任务有效期
     */
    private Integer taskDay;
    private Integer taskHour;
    private Integer taskMinute;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件关联key
     * (不直接用id关联是因为用单独的key可以直接知道是否有文件,没有就不用去查询了)
     */
    private Long fileRefKey;

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
    private BigDecimal requireProgress;

    /**
     * 任务类型（普通、标准、对象）
     */
    private String taskType;
}
