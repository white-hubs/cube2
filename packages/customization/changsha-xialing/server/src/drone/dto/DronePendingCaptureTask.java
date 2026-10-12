package com.guhecloud.cube.customization.xialing.server.drone.dto;

import lombok.Data;

import java.math.BigDecimal;

/** Redis 中等待到点触发的报警派飞拍摄任务。 */
@Data
public class DronePendingCaptureTask {
    /** 派飞日志ID。 */
    private Long dispatchRecordId;
    /** 租户ID。 */
    private Integer tenant;
    /** 机库编号。 */
    private String nestSn;
    /** 目标经度。 */
    private BigDecimal targetLongitude;
    /** 目标纬度。 */
    private BigDecimal targetLatitude;
    /** 执行动作：photo 拍照，video 录像。 */
    private String mediaAction;
    /** 云台挂载类型。 */
    private Integer gimbalYtType;
    /** 到点距离阈值（米）。 */
    private BigDecimal arrivalDistanceThresholdMeters;
    /** 到点水平速度阈值（厘米/秒）。 */
    private BigDecimal arrivalSpeedThresholdCmps;
    /** 录像持续时长（秒）。 */
    private Integer videoDurationSeconds;
}
