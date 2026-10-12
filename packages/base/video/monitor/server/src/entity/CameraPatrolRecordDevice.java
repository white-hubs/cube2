package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceResultEnum;
import com.guhecloud.cube.biz.vmonitor.server.enumeration.CameraPatrolDeviceStatusEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 视频巡逻设备记录数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:46
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolRecordDevice extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 巡逻监控设备主键
     */
    private Long deviceId;

    /**
     * 巡逻监控设备编号
     */
    private String deviceCode;

    /**
     * 巡逻监控设备名称
     */
    private String deviceName;

    /**
     * 巡逻设备区域
     */
    private String area;

    /**
     * 巡逻设备区域名称
     */
    private String areaNames;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 巡逻时间
     */
    private LocalDateTime patrolTime;

    /**
     * 巡逻结果
     */
    private CameraPatrolDeviceResultEnum result = CameraPatrolDeviceResultEnum.CAMERA_PATROL_UNKNOWN;

    /**
     * 巡逻状态
     */
    private CameraPatrolDeviceStatusEnum status = CameraPatrolDeviceStatusEnum.CAMERA_PATROL_UNKNOWN;

    /**
     * 异常数量
     */
    private Integer abnormalNum;
}