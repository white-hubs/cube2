package com.guhecloud.cube.biz.daily.task.param;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/12/24 11:13
 * @Description:
 */
@Data
public class DailyTaskAssignUserParam {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 任务百分比
     *
     * @return
     */
    private BigDecimal taskPercent;
}
