package com.guhecloud.cube.biz.daily.task.commons.service.record.enhance;

import com.guhecloud.cube.biz.daily.task.commons.constants.DailyConstants;
import com.guhecloud.cube.biz.daily.task.commons.param.DailyRecordSubmitAfterParam;

/**
 * 记录提交后置处理
 */
@FunctionalInterface
public interface DailyRecordSubmitAfterHandler {
    /**
     * 记录提交后置处理
     *
     * @param param 提交参数
     */
    void handle(DailyRecordSubmitAfterParam param);

    /**
     * 默认返回
     */
    default String getType() {
        return DailyConstants.DAILY_TASK;
    }
}
