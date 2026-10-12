package com.guhecloud.cube.srv.gen.medical.cleanliness.server.resources.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (手术室)物品
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SurgeryItem extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private String deviceCode;
    private String deviceName;
    /**
     * 名称
     */
    private String name;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 数量
     */
    private String number;
    /**
     * 厂家
     */
    private String manufacturer;
    /**
     * 型号
     */
    private String model;
    /**
     * 机组id
     */
    private Long machinesId;
    /**
     * 机组编号
     */
    private String machinesCode;
}
