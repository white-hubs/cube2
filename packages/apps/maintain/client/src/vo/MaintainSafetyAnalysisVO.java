package com.guhecloud.cube.srv.gen.maintain.vo;

import lombok.Data;

@Data
public class MaintainSafetyAnalysisVO {

    /**
     * 隐患处理率统计
     */
    private WorkSituationVO hiddenDangerDispose;

    /**
     * 安全巡更频率
     */
    private WorkSituationVO patrolFrequency;
    /**
     * 安全巡检频率
     */
    private WorkSituationVO inspectFrequency;
}
