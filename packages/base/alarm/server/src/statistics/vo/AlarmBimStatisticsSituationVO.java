package com.guhecloud.cube.base.alarm.server.statistics.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警态势统计返回参数
 *
 * @author lanchun
 * @date 2023/5/10 11:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsSituationVO {

    /**
     * 报警数统计
     */
    private AlarmBimStatisticsVO alarmBimStatistics;

    /**
     * 报警类型
     */
    private List<AlarmBimStatisticsTopChainVO> alarmBimStatisticsTopChains;
}
