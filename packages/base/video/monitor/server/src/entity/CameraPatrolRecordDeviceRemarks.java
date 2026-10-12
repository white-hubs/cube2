package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 视频巡逻设备记录备注
 *
 * @author lanchun
 * @date 2022/8/4 14:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolRecordDeviceRemarks extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 巡逻记录主键
     */
    private Long cameraPatrolRecordId;

    /**
     * 视频巡逻设备记录主键
     */
    private Long cameraPatrolRecordDeviceId;

    /**
     * 巡逻监控设备主键
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
     * 序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 附件地址字符串
     */
    private String filePaths;

}
