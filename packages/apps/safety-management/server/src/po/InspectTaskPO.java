package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 日常巡检
 * @date 2023/9/7 15:21
 */
@Data
@TableName("safety_inspect_task")
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectTaskPO extends BasicEnhancedTenantEntity<Long> {
    /**
     * 任务id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务创建时间
     */
    private LocalDateTime taskCreateTime;
    /**
     * 巡检类型
     */
    private String typeName;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 执行级别
     */
    private String levelCode;
    private String levelName;
    /**
     * 执行状态
     */
    private String statusCode;
    private String statusName;
    /**
     * 执行人
     */
    private String executor;
    /**
     * 生效结束时间
     */
    private LocalDateTime validEndTime;
    /**
     * 目标对象数
     */
    private Integer targetNum;
    /**
     * 执行进度
     */
    private Double progress;
    /**
     * 所属市场
     */
    private Integer marketTenant;
    private String marketName;
    private String businessType;
}
