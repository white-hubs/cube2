package com.guhecloud.cube.biz.daily.task.configuration.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务周期响应
 */
@Data
public class DailyCronResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 周期名称
     */
    private String name;

    /**
     * cron表达式
     */
    private String cron;

    /**
     * 描述
     */
    private String description;

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

