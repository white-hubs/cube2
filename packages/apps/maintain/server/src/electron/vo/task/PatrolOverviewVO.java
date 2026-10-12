package com.guhecloud.cube.srv.gen.maintain.server.electron.vo.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 巡更概览
 * @date 2023/5/29 14:04
 */
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatrolOverviewVO {
    /**
     * 总数
     */
    private Long total;
    /**
     * 待接收数
     */
    private Long toBeReceived;
    /**
     * 巡检汇总数
     */
    private Long inExecution;
    /**
     * 已结束数
     */
    private Long finished;
}
