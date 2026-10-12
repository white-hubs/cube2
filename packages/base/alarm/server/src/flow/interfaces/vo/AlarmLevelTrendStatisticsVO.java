package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警等级趋势统计返回参数
 *
 * @author lanchun
 * @date 2022/6/7 14:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmLevelTrendStatisticsVO {

    /**
     * 报警等级集合
     */
    private List<AlarmLevelStatisticsVO> alarmLevelStatisticsVOS;

    /**
     * 时间
     */
    private String period;
}
