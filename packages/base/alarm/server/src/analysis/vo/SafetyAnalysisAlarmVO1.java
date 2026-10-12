package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SafetyAnalysisAlarmVO1 {

    /**
     * 各应用报警数和占比
     */
    private List<AlarmLevelRatioCardVO> applicationAlarmDataList;

    /**
     * 安全任务报警数
     */
    private Long safetyAlarmNum;

    /**
     * 小散工程数
     */
    private Long looseWorkNum;

    /**
     * 风险源建档数
     */
    private Long riskSourceNum;
}
