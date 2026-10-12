package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity;

import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 事件联动摄像机执行记录
 *
 * @author lanchun
 * @date 2022/6/27 17:16
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class EventVideoRecord extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 事件关联联动主键
     */
    private Long eventLinkageId;

    /**
     * 事件主键
     */
    private Long eventId;

    /**
     * 联动主键
     */
    private Long linkageId;

    /**
     * 设备主键
     */
    private Long deviceId;

    /**
     * 设备编号
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 摄像机token
     */
    private String token;

    /**
     * 执行模型
     */
    private ExecuteModeEnum executeMode;

    /**
     * 执行状态
     */
    private ExecuteStatusEnum executeStatus;

    /**
     * 执行时间
     */
    private LocalDateTime executeTime;

    /**
     * 摄像机动作类型
     */
    private VideoActionTypeEnum type;

    /**
     * 动作时长
     */
    private Integer time;

    /**
     * 动作时长单位
     */
    private String timeUnit;

    /**
     * 间隔时间
     */
    private Integer intervalTime;

    /**
     * 持续次数
     */
    private Integer durationTime;

    /**
     * 操作人
     */
    private String operatorBy;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime operatorTime;
}
