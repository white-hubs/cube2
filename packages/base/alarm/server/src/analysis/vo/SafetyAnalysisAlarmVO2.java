package com.guhecloud.cube.base.alarm.server.analysis.vo;

import com.guhecloud.cube.base.alarm.server.util.MathUtils;
import lombok.Data;

@Data
public class SafetyAnalysisAlarmVO2 {

    /**
     * 当前类型的报警数
     */
    private Long alarmNum;

    /**
     * 类型
     */
    private String typeCode;

    /**
     * 已处理报警数
     */
    private Long handlerNum;

    /**
     * 真实报警数
     */
    private Long alarmActualNum;

    /**
     * 真实报警率
     */
    private Float correctRate;

    public Float getCorrectRate() {
        return MathUtils.calculateProportion(alarmActualNum, handlerNum);
    }
}
