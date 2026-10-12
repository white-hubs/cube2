package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警事件分析返回参数
 *
 * @author lanchun
 * @date 2022/11/9 16:33
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEventAnalysisStatisticsVO {

    /**
     * 排名前三事件名称
     */
    private String alarmTopThreeEventNames;

    /**
     * 排名第一事件名称
     */
    private String alarmTopEventName;

    /**
     * 排名第一事件数量
     */
    private Integer alarmTopNum;

    /**
     * 事件分组统计
     */
    private List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS;
}
