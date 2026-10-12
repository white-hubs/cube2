package com.guhecloud.cube.biz.daily.task.configuration.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 紧急程度响应
 */
@Data
public class DailyUrgencyResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 紧急程度等级
     */
    private Integer level;

    /**
     * 紧急程度名称
     */
    private String name;

    /**
     * 任务类型
     */
    private String dailyType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
