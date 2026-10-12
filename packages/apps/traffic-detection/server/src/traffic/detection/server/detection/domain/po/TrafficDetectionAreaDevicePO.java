package com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 人流区域设备关联表
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("traffic_detection_area_device")
public class TrafficDetectionAreaDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 人流区域id
     */
    private Long trafficDetectionAreaId;

    /**
     * 设备id
     */
    private Long deviceId;

    /**
     * 设备配置
     */
    private String deviceConfig;
}
