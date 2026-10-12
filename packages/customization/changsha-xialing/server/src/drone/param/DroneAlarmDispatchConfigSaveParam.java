package com.guhecloud.cube.customization.xialing.server.drone.param;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报警派飞配置保存参数。
 */
@Data
public class DroneAlarmDispatchConfigSaveParam {
    /** 是否启用自动派飞。 */
    private Boolean enabled;
    /** 触发派飞的报警对象快照。 */
    private List<IdNameParam> alarmTargets;
    /** 触发派飞的报警级别快照。 */
    private List<IdNameParam> alarmLevels;
    /** 触发派飞的报警类型快照。 */
    private List<IdNameParam> alarmTypes;
    /** 执行派飞的无人机设备ID。 */
    private Long droneDeviceId;
    /** 执行派飞的无人机设备名称快照。 */
    private String droneDeviceName;
    /** 快速任务完成动作。 */
    private Integer finishAction;
    /** 快速任务飞行高度。 */
    private BigDecimal flyHeight;
    /** 快速任务场景ID，可选。 */
    private Long sceneId;
    /** 快速任务场景编号，可选。 */
    private String sceneCode;
    /** 到点执行动作：photo 拍照，video 录像，空表示不执行。 */
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
