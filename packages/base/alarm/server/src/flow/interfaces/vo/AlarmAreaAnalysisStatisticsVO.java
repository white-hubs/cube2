package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警区域分析返回参数
 *
 * @author lanchun
 * @date 2022/11/9 16:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmAreaAnalysisStatisticsVO {

    /**
     * 排名前三区域名称
     */
    private String alarmTopThreeAreaNames;

    /**
     * 排名第一区域名称
     */
    private String alarmTopAreaName;

    /**
     * 排名第一区域数量
     */
    private Integer alarmTopNum;

    /**
     * 区域分组统计
     */
    private List<AlarmCurrencyStatisticsVO> alarmCurrencyStatisticsVOS;
}
