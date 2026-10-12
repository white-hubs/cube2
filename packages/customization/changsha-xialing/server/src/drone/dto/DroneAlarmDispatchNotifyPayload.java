package com.guhecloud.cube.customization.xialing.server.drone.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 报警派飞结果消息业务数据。
 */
@Data
public class DroneAlarmDispatchNotifyPayload {
    /** 报警记录ID。 */
    private Long alarmRecordId;
    /** 派飞日志ID。 */
    private Long dispatchRecordId;
    /** 派飞状态：1成功，2失败。 */
    private Integer dispatchStatus;
    /** 是否派飞成功。 */
    private Boolean success;
    /** 展示文案。 */
    private String message;
    /** 失败原因。 */
    private String failReason;
    /** 快速任务名称。 */
    private String taskName;
    /** 报警对象名称。 */
    private String alarmTargetName;
    /** 派飞时间。 */
    private LocalDateTime dispatchTime;
}
