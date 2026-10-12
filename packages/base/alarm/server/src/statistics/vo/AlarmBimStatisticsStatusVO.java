package com.guhecloud.cube.base.alarm.server.statistics.vo;

import com.guhecloud.cube.base.alarm.server.flow.interfaces.vo.AlarmStatusStatisticsVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警状态统计
 *
 * @author lanchun
 * @date 2023/3/16 14:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmBimStatisticsStatusVO {

    /**
     * 报警数量
     */
    private Long alarmNum;

    /**
     * 环比增长率
     */
    private double chainRatio;

    /**
     * 状态分组统计集合
     */
    private List<AlarmStatusStatisticsVO> alarmStatusStatistics;
}
