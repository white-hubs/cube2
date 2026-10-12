package com.guhecloud.cube.biz.daily.task.task.convert;

import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/18 10:07
 * @Description:
 */
public class DailyTaskAddParamCovert {
    private DailyTaskAddParamCovert() {}

    public static DailyTaskAddParam covertNew(DailyTaskAddParam dailyTaskAddParam) {
        DailyTaskAddParam newDailyTaskAddParam = new DailyTaskAddParam();
        newDailyTaskAddParam.setDailyType(dailyTaskAddParam.getDailyType());
        newDailyTaskAddParam.setTaskCategoryId(dailyTaskAddParam.getTaskCategoryId());
        newDailyTaskAddParam.setUrgencyId(dailyTaskAddParam.getUrgencyId());
        newDailyTaskAddParam.setTaskDay(dailyTaskAddParam.getTaskDay());
        newDailyTaskAddParam.setTaskHour(dailyTaskAddParam.getTaskHour());
        newDailyTaskAddParam.setTaskMinute(dailyTaskAddParam.getTaskMinute());
        newDailyTaskAddParam.setDescription(dailyTaskAddParam.getDescription());
        newDailyTaskAddParam.setMethod(dailyTaskAddParam.getMethod());
        newDailyTaskAddParam.setIsOnDuty(dailyTaskAddParam.getIsOnDuty());
        newDailyTaskAddParam.setRequireProgress(dailyTaskAddParam.getRequireProgress());
        newDailyTaskAddParam.setFileIds(dailyTaskAddParam.getFileIds());
        newDailyTaskAddParam.setTaskItemIdList(dailyTaskAddParam.getTaskItemIdList());
        newDailyTaskAddParam.setPublisherId(dailyTaskAddParam.getPublisherId());
        newDailyTaskAddParam.setPlanId(dailyTaskAddParam.getPlanId());
        newDailyTaskAddParam.setName(dailyTaskAddParam.getName());
        newDailyTaskAddParam.setTaskType(dailyTaskAddParam.getTaskType());
        newDailyTaskAddParam.setContinuity(dailyTaskAddParam.getContinuity());
        return newDailyTaskAddParam;
    }
}
