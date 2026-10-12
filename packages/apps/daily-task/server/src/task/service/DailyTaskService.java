package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyStandardResultDTO;
import com.guhecloud.cube.biz.daily.task.commons.dto.DailyTargetDTO;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyPlanTargetResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskDetailResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskHomePageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskPageResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskSummaryResult;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskTargetResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskPO;
import com.guhecloud.cube.biz.mdata.client.rocketmq.message.GridAreaMemberSetMessage;
import com.guhecloud.cube.biz.daily.task.param.DailyBaseStatisticsParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAddParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskAssignParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskFeedbackParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOperationParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskOtherTargetParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskPageParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskSubmitParam;
import com.guhecloud.cube.biz.daily.task.param.DailyTaskTargetQueryParam;
import com.guhecloud.cube.biz.daily.task.status.base.TransitionContext;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DailyTaskService extends IService<DailyTaskPO> {
    Pagination<DailyTaskPageResult> pageDailyTask(DailyTaskPageParam param);

    DailyTaskDetailResult detailDailyTask(Long id);

    Long createDailyTask(DailyTaskAddParam param);

    /**
     * 批量添加多个任务
     */

    List<Long> createDailyTaskBatch(List<DailyTaskAddParam> paramList);

    List<DailyTargetDTO> getTaskOtherTarget(@RequestBody DailyTaskOtherTargetParam param);

    boolean checkTaskCompleted(TransitionContext context);

    boolean checkTaskDistribute(TransitionContext context);

    Boolean transferDailyTask(DailyTaskOperationParam param);

    Boolean distributeDailyTask(DailyTaskOperationParam param);

    boolean checkTaskDistributeComplete(TransitionContext context);

    Pagination<DailyTaskTargetResult> relateDailyTask(DailyTaskTargetQueryParam param);

    Boolean editDailyTask(DailyTaskAddParam param);

    Boolean deleteDailyTask(Long id, Set<Long> refIds, List<Long> taskItemIdList);

    List<DailyStandardResultDTO> getDailyTaskByTarget(Long taskId, Long targetId);

    List<Integer> getTaskTenantList();

    List<String> getTaskType();

    List<DailyTaskPO> getOverDueTask(List<MessageSendConfig> value);

    List<DailyTaskPO> getTaskWillOverOrProgress(List<MessageSendConfig> value);

    Map<String, List<DailyPlanTargetResult>> relateDailyTaskApp(DailyTaskTargetQueryParam param);

    /**
     * 获取任务进度
     *
     * @param taskIdList
     *
     * @return
     */
    Map<Long, BigDecimal> getTaskProgress(List<Long> taskIdList);

    DailyTaskHomePageResult statisticsDailyTask(DailyBaseStatisticsParam param);

    void updateTaskProgress(TransitionContext context);

    Boolean terminateDailyTask(Long id);

    Map<Long, Set<Long>> getTaskTargetUserMap(List<Long> taskIdList);

    Map<Long, Long> getGridAbnormalTaskCount(List<Long> gridAreaIds, String dailyType);

    DailyTaskSummaryResult getDailyTaskSummary(long taskId);

    Boolean submitDailyTask(DailyTaskSubmitParam param);

    List<Long> getDailyTaskTargetIdList(Long taskId);

    Boolean assignDailyTask(DailyTaskAssignParam assignParam);

    BigDecimal getDailyTaskPercent(Long taskId);

    List<DailyPlanTargetResult> relateDailyTaskPc(DailyTaskTargetQueryParam param);

    DailyTaskTargetResult getDailyTaskTargetRelate(Long taskId, Long id);

    /**
     * 任务反馈
     */
    Boolean feedbackDailyTask(DailyTaskFeedbackParam param);

    /**
     * 处理网格员设置消息，补发当前网格未完成任务负责人和待办
     */
    void handleGridAreaMemberSet(GridAreaMemberSetMessage message);
}
