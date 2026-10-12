package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/** 报警自动派飞当前配置。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_alarm_dispatch_config")
public class DroneAlarmDispatchConfig extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID。 */
    private Long id;
    /** 是否启用自动派飞。 */
    private Boolean enabled;
    /** 报警对象ID快照JSON。 */
    private String alarmTargetIds;
    /** 报警对象名称快照JSON。 */
    private String alarmTargetNames;
    /** 报警级别ID快照JSON。 */
    private String alarmLevelIds;
    /** 报警级别名称快照JSON。 */
    private String alarmLevelNames;
    /** 报警类型ID快照JSON。 */
    private String alarmTypeIds;
    /** 报警类型名称快照JSON。 */
    private String alarmTypeNames;
    /** 执行派飞的无人机设备ID。 */
    private Long droneDeviceId;
    /** 执行派飞的无人机设备名称快照。 */
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
