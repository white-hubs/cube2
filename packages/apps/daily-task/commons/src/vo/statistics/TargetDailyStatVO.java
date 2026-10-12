package com.guhecloud.cube.biz.daily.task.commons.vo.statistics;

import lombok.Data;

@Data
public class TargetDailyStatVO {

    /**
     * 计划数
     */
    private long planNum;

    /**
     * 任务数
     */
    private long taskNum;

    /**
     * 记录异常数
     */
    private long abnormalNum;
}
