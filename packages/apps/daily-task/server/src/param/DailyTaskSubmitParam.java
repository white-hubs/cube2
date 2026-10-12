package com.guhecloud.cube.biz.daily.task.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/18 10:23
 * @Description:
 */
@Data
public class DailyTaskSubmitParam {

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 任务类型
     */
    private String dailyType;
}
