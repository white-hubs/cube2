package com.guhecloud.cube.base.alarm.server.flow.interfaces.vo;

import lombok.Data;

@Data
public class AlarmRecordHandlerReasonVO {

    private Long id;

    /**
     * 报警记录主键
     */
    private Long alarmRecordId;

    /**
     * 报警原因主键
     */
    private Long alarmReasonId;

    /**
     * 报警原因编号
     */
    private String alarmReasonCode;

    /**
     * 报警原因
     */
    private String alarmReason;

    /**
     * 报警原因描述
     */
    private String alarmReasonRemark;
}
