package com.guhecloud.cube.srv.gen.safety.management.dto.vo.statistics.alarm;

import lombok.Data;

import java.util.List;

@Data
public class AlarmStatusTrendAnalysisVO {

    /**
     * 报警数量趋势
     */
    private List<AlarmTrendVO> alarmTrends;

    /**
     * 报警已处理数量趋势
     */
    private List<AlarmTrendVO> alarmHandlerTrends;
}
