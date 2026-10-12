package com.guhecloud.cube.customization.xialing.server.drone.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 报警派飞日志展示对象。
 */
@Data
public class DroneAlarmDispatchRecordVO {
    /** 派飞日志主键。 */
    private Long id;
    /** 报警记录ID。 */
    private Long alarmRecordId;
    /** 报警编号。 */
    private String alarmCode;
    /** 报警对象ID。 */
    private Long alarmTargetId;
    /** 报警对象名称。 */
    private String alarmTargetName;
    /** 报警级别ID。 */
    private Long alarmLevelId;
    /** 报警级别名称。 */
    private String alarmLevelName;
    /** 报警类型ID。 */
    private Long alarmTypeId;
    /** 报警类型名称。 */
    private String alarmTypeName;
    /** 无人机设备ID。 */
    private Long droneDeviceId;
    /** 无人机设备名称。 */
    private String droneDeviceName;
    /** 机库编号。 */
    private String nestSn;
    /** 目标经度。 */
    private BigDecimal longitude;
    /** 目标纬度。 */
    private BigDecimal latitude;
    /** 快速任务名称。 */
    private String taskName;
    /** 派飞状态：1成功，2失败。 */
    private Integer dispatchStatus;
    /** 失败原因。 */
    private String failReason;
    /** 航迹文件类型。 */
    private String trackFileType;
    /** 航迹ID。 */
    private String trackId;
    /** 航迹URL。 */
    private String trackUrl;
    /** 机库名称。 */
    private String nestName;
    /** 快速任务请求体。 */
    private String requestBody;
    /** 三方响应体。 */
    private String responseBody;
    /** 到点执行动作：photo 拍照，video 录像，空表示不执行。 */
    private String mediaAction;
    /** 媒体触发状态：1待触发，2成功，3失败，4无需触发。 */
    private Integer mediaStatus;
    /** 媒体触发失败原因。 */
    private String mediaFailReason;
    /** 媒体触发请求体。 */
    private String mediaRequestBody;
    /** 媒体触发响应体。 */
    private String mediaResponseBody;
    /** 媒体触发时间。 */
    private LocalDateTime mediaTriggerTime;
    /** 派飞时间。 */
    private LocalDateTime dispatchTime;
}
