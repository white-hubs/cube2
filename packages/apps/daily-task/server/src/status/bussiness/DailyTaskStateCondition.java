package com.guhecloud.cube.biz.daily.task.status.bussiness;

import com.guhecloud.cube.biz.daily.task.status.base.StateCondition;
import com.guhecloud.cube.biz.daily.task.task.service.DailyTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/12 10:26
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DailyTaskStateCondition {
    private final DailyTaskService dailyTaskService;

    public StateCondition completedCondition() {
        return dailyTaskService::checkTaskCompleted;
    }

    public StateCondition distributeStatus() {

        return dailyTaskService::checkTaskDistribute;
    }

    public StateCondition distributeCompleteStatus() {
        return dailyTaskService::checkTaskDistributeComplete;
    }
}
