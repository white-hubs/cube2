package com.guhecloud.cube.biz.daily.task.record.param;

import lombok.Data;

/**
 * 记录详情入参对象
 * 通过id或者任务ID+对象ID查询记录详情
 */
@Data
public class DailyRecordDetailParam {
    /**
     * 记录ID
     */
    private Long id;

    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 目标ID
     */
    private Long targetId;
}
