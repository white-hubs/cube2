package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics;

import lombok.Data;

/**
 * 任务状态统计
 *
 * @author lanchun
 * @date 2024/1/25 10:25
 */
@Data
public class TaskStatusStatisticsVO {

    /**
     * 执行中数量
     */
    private Integer inExecutionNumber;

    /**
     * 待接收总数
     */
    private Integer toBeReceivedNumber;

    /**
     * 已结束总数
     */
    private Integer finishedNumber;

    /**
     * 异常结束总数
     */
    private Integer abnormalFinishedNumber;
}
