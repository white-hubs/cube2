package com.guhecloud.cube.biz.daily.task.commons.service;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 20:11
 * @Description:
 */
public interface TaskCommonFields {
    Long getId();

    String getDailyType();

    String getName();

    String getStatus();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    Long getTaskCategoryId();

    Long getUrgencyId();

    Integer getTaskDay();

    Integer getTaskHour();

    Integer getTaskMinute();

    String getDescription();

    Long getFileRefKey();

}
