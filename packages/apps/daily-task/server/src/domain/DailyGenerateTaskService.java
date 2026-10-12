package com.guhecloud.cube.biz.daily.task.domain;

import java.time.LocalDateTime;

public interface DailyGenerateTaskService {
    void batchGenerateTaskByPlan(LocalDateTime now);
}
