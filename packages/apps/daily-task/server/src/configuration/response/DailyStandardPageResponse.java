package com.guhecloud.cube.biz.daily.task.configuration.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 执行标准响应
 */
@Data
public class DailyStandardPageResponse {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 标准名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 任务种类ID（多个ID用逗号分隔，如：1,2,3）
     */
    private String categoryIds;

    /**
     * 任务种类名称（多个名称用逗号分隔，如：类型1,类型2,类型3）
     */
    private String categoryNames;

    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 关联对象字符串
     */
    private String targetTypeRelationStr;

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

