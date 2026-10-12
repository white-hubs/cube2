package com.guhecloud.cube.srv.gen.safety.management.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * @author wql
 * @describe 安全巡检列表详情
 * @date 2023/9/7 20:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InspectTaskVO {
    private Integer marketTenant;
    /**
     * 所属市场
     */
    private String marketName;
    /**
     * 任务名称
     */
    private String name;
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
    private String level;
    /**
     * 执行状态
     */
    private String status;
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
    private String progressRate;
}
