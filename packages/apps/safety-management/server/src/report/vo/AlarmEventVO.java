package com.guhecloud.cube.srv.gen.safety.management.report.vo;

import lombok.Data;

@Data
public class AlarmEventVO {
    /**
     * 排名
     */
    private Integer index;
    /**
     * 报警事件名称
     */
    private String alarmEventName;
    /**
     * 报警数据
     */
    private Long alarmNum;
    /**
     * 报警占比
     */
    private String alarmProportion;
    /**
     * 已处理报警
     */
    private Long processed;
    /**
     * 处理率
     */
    private String processedRate;
    /**
     * 报警正确率
     */
    private String accuracy;
}
