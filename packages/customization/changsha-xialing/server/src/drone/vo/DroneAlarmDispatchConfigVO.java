package com.guhecloud.cube.customization.xialing.server.drone.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 报警派飞配置展示对象。
 */
@Data
public class DroneAlarmDispatchConfigVO {
    /** 配置主键。 */
    private Long id;
    /** 是否启用自动派飞。 */
    private Boolean enabled;
    /** 报警对象回显快照。 */
    private List<IdNameVO> alarmTargets;
    /** 报警级别回显快照。 */
    private List<IdNameVO> alarmLevels;
    /** 报警类型回显快照。 */
    private List<IdNameVO> alarmTypes;
    /** 无人机设备ID。 */
    private Long droneDeviceId;
    /** 无人机设备名称快照。 */
    private String droneDeviceName;
    /** 快速任务完成动作。 */
    private Integer finishAction;
    /** 快速任务飞行高度。 */
    private BigDecimal flyHeight;
    /** 快速任务场景ID。 */
    private Long sceneId;
    /** 快速任务场景编号。 */
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
