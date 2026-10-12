package com.guhecloud.cube.biz.daily.task.param;

import lombok.Data;

import java.util.List;

@Data
public class DailyTaskOtherTargetParam {
    /**
     * 任务id
     */
    private Long taskId;
    /**
     * 对象(档位)id
     */
    private Long targetId;
    /**
     * 执行项id集合
     */
    private List<Long> itemIdList;
}
