package com.guhecloud.cube.biz.daily.task.plan.coverts;

import com.guhecloud.cube.biz.daily.task.commons.po.DailyRecordPO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyRecordResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskDetailResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyPlanPO;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.daily.task.enums.DailyPlanStatusEnum;
import com.guhecloud.cube.biz.daily.task.param.DailyPlanCommandParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.record.result.DailyRecordDetail;

import java.util.Objects;

public class DailyModelConverterUtil {

    /**
     * 计划 param转po
     *
     * @param param 入参
     *
     * @return po
     */
    public static DailyPlanPO dailyPlanCommandParamToPo(DailyPlanCommandParam param) {
        var po = new DailyPlanPO();
        po.setDailyType(param.getDailyType());
        po.setName(param.getName());
        po.setId(param.getId());
        po.setDailyType(param.getDailyType());
        po.setName(param.getName());
        po.setStatus(DailyPlanStatusEnum.RUNNING.name());
        po.setDescription(param.getDescription());
        po.setCronId(param.getCronId());
        po.setTaskCategoryId(param.getTaskCategoryId());
        po.setUrgencyId(param.getUrgencyId());
        po.setExecuteWay(param.getExecuteWay());
        po.setExecutionCount(0);
        po.setStartTime(param.getStartTime());
        po.setEndTime(param.getEndTime());
        po.setTaskDay(param.getTaskDay());
        po.setTaskHour(param.getTaskHour());
        po.setTaskMinute(param.getTaskMinute());
        po.setSpecifiedType(param.getSpecifiedType());
        po.setSpecifiedDays(param.getSpecifiedDays());
        po.setSpecifiedTime(param.getSpecifiedTime());
        po.setMethod(param.getMethod());
        po.setIsOnDuty(param.getIsOnDuty());
        po.setRequireProgress(param.getRequireProgress());
        po.setTaskType(Objects.nonNull(param.getTaskType()) ? param.getTaskType().name() : null);
        return po;
    }

    /**
     * 计划 po转result
     *
     * @param po 入参
     *
     * @return result
     */
    public static DailyPlanDetailResult dailyPlanPoToResult(DailyPlanPO po) {
        var result = new DailyPlanDetailResult();
        result.setId(po.getId());
        result.setDailyType(po.getDailyType());
        result.setName(po.getName());
        result.setStatus(po.getStatus());
        result.setDescription(po.getDescription());
        result.setCronId(po.getCronId());
        result.setTaskCategoryId(po.getTaskCategoryId());
        result.setUrgencyId(po.getUrgencyId());
        result.setExecuteWay(po.getExecuteWay());
        result.setFileRefKey(po.getFileRefKey());
        result.setExecutionCount(po.getExecutionCount());
        result.setStartTime(po.getStartTime());
        result.setEndTime(po.getEndTime());
        result.setTaskDay(po.getTaskDay());
        result.setTaskHour(po.getTaskHour());
        result.setTaskMinute(po.getTaskMinute());
        result.setSpecifiedType(po.getSpecifiedType());
        result.setSpecifiedDays(po.getSpecifiedDays());
        result.setSpecifiedTime(po.getSpecifiedTime());
        result.setCreateTime(po.getCreateTime());
        result.setCreateBy(po.getCreateBy());
        result.setIsOnDuty(po.getIsOnDuty());
        result.setRequireProgress(po.getRequireProgress());
        result.setMethod(po.getMethod());
        result.setTaskType(po.getTaskType());
        return result;
    }

    /**
     * 任务 param转po
     *
     * @param param 入参
     *
     * @return po
     */
    public static DailyTaskPO dailyTaskAddParamToPo(DailyTaskAddParam param) {
        var po = new DailyTaskPO();
        po.setDailyType(param.getDailyType());
        po.setName(param.getName());
        po.setParentId(param.getParentId());
        return po;
    }

    /**
     * 任务 po转result
     *
     * @param po 入参
     *
     * @return result
     */
    public static DailyTaskDetailResult dailyTaskPoToResult(DailyTaskPO po) {
        var result = new DailyTaskDetailResult();
        result.setId(po.getId());
        result.setDailyType(po.getDailyType());
        result.setName(po.getName());
        return result;
    }

    public static DailyRecordDetail dailyRecordPoToResult(DailyRecordPO po) {
        var dailyRecordDetail = new DailyRecordDetail();
        dailyRecordDetail.setDailyType(po.getDailyType());
        dailyRecordDetail.setTaskId(po.getTaskId());
        dailyRecordDetail.setTaskName(po.getTaskName());
        dailyRecordDetail.setExecutionTime(po.getExecutionTime());
        dailyRecordDetail.setExecutorId(po.getExecutorId());
        dailyRecordDetail.setExecutorName(po.getExecutorName());
        dailyRecordDetail.setExecutionResult(po.getExecutionResult());
        dailyRecordDetail.setRecordStatus(po.getRecordStatus());
        return dailyRecordDetail;
    }

    public static DailyRecordResult dailyRecordPoToNormalResult(DailyRecordPO po) {
        var dailyRecordDetail = new DailyRecordResult();
        dailyRecordDetail.setDailyType(po.getDailyType());
        dailyRecordDetail.setTaskId(po.getTaskId());
        dailyRecordDetail.setTaskName(po.getTaskName());
        dailyRecordDetail.setExecutionTime(po.getExecutionTime());
        dailyRecordDetail.setExecutorId(po.getExecutorId());
        dailyRecordDetail.setExecutorName(po.getExecutorName());
        dailyRecordDetail.setExecutionResult(po.getExecutionResult());
        dailyRecordDetail.setRecordStatus(po.getRecordStatus());
        dailyRecordDetail.setRemark(po.getRemark());
        return dailyRecordDetail;
    }
}
