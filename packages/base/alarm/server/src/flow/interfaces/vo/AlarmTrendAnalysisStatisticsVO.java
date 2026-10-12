package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警趋势分析返回参数
 *
 * @author lanchun
 * @date 2022/11/14 16:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlarmTrendAnalysisStatisticsVO {

    /**
     * 最高时间段
     */
    private String topTimeSlot;

    /**
     * 趋势列表
     */
    private List<AlarmTrendStatisticsVO> alarmTrendStatisticsVOS;
}
