package com.guhecloud.cube.biz.daily.task.param;

import lombok.Data;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/11/13 11:31
 * @Description: 任务分发参数
 */
@Data
public class DailyTaskDistributeParam {
    /**
     * 任务id
     */
    private Long taskId;

    /**
     * 分发人
     */
    private Long userId;

    /**
     * 分发对象id
     */
    private List<Long> targetIdList;

}
