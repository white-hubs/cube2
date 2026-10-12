package com.guhecloud.cube.biz.daily.task.statistics.service;

import com.guhecloud.platform.base.model.Period;

public interface DailyTargetStatService {

    /**
     * 获取目标计划数
     *
     * @param targetId  目标ID
     * @param dailyType 日常类型
     * @param period    时间范围
     *
     * @return 目标计划数
     */
    Integer getTargetPlanNum(Long targetId, String dailyType, Period period);

    /**
     * 获取目标任务数
     *
     * @param targetId  目标ID
     * @param dailyType 日常类型
     * @param period    时间范围
     *
     * @return 目标任务数
     */
    Integer getTargetTaskNum(Long targetId, String dailyType, Period period);

    /**
     * 获取目标异常数
     *
     * @param targetId  目标ID
     * @param dailyType 日常类型
     * @param period    时间范围
     *
     * @return 目标异常数
     */
    Integer getTargetAbnormalNum(Long targetId, String dailyType, Period period);
}
