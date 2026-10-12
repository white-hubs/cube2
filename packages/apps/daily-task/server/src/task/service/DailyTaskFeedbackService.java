package com.guhecloud.cube.biz.daily.task.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.daily.task.commons.result.DailyTaskFeedbackResult;
import com.guhecloud.cube.biz.daily.task.domain.po.DailyTaskFeedbackPO;

import java.util.List;

public interface DailyTaskFeedbackService extends IService<DailyTaskFeedbackPO> {

    /**
     * 根据任务id查询反馈列表
     */
    List<DailyTaskFeedbackResult> getFeedbackListByTaskId(Long taskId);
}
