package com.guhecloud.cube.biz.linkage.server.record.reconfirmation.application;

import com.guhecloud.cube.biz.linkage.server.record.reconfirmation.domain.entity.EventJudgmentDelayTask;

public interface EventJudgmentDelayQueueService {

    /**
     * 提交延时任务
     *
     * @param eventJudgmentDelayTask 任务信息
     */
    void submitDelayTask(EventJudgmentDelayTask eventJudgmentDelayTask);

    /**
     * 取消延时任务
     *
     * @param id 报警ID
     */
    void cancelDelayTask(Long id);
}
