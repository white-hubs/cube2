package com.guhecloud.cube.base.alarm.server.analysis.vo;

import lombok.Data;

@Data
public class AlarmLevelRatioCardVO {

    /**
     * 总数
     */
    private Long alarmTotalNum;

    /**
     * 当前类型的报警数
     */
    private Long alarmNum;

    /**
     * 类型
     */
    private String typeCode;
    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 占比
     */
    private Float Ratio;

}
