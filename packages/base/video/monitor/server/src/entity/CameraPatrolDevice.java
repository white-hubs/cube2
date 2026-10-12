package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 巡逻监控数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CameraPatrolDevice extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 巡逻线路主键
     */
    private Long cameraPatrolLineId;

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

}