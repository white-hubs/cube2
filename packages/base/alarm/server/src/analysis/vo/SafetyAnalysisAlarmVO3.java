package com.guhecloud.cube.base.alarm.server.analysis.vo;

import com.guhecloud.cube.srv.gen.maintain.vo.WorkSituationVO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SafetyAnalysisAlarmVO3 {

    /**
     * 报警任务响应率统计
     */
    private WorkSituationVO alarmDispose;
    /**
     * 隐患处理率统计
     */
    private WorkSituationVO hiddenDangerDispose;

    /**
     * 风险源建档率统计
     */
    private WorkSituationVO riskSourcesFillDispose;

    /**
     * 小散工程异常率
     */
    private WorkSituationVO projectError;
    /**
     * 安全巡更频率
     */
    private WorkSituationVO patrolFrequency;
    /**
     * 安全巡检频率
     */
    private WorkSituationVO inspectFrequency;
    /**
     * 安全预案制度
     */
    private WorkSituationVO emergencyFile;
    /**
     * 安全教育频率
     */
    private WorkSituationVO educationFrequency;
}

