package com.guhecloud.cube.biz.daily.task.configuration.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 任务类别响应
 */
@Data
public class DailyTaskCategoryResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务类型名称
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
