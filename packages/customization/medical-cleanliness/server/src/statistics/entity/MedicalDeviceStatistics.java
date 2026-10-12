package com.guhecloud.cube.srv.gen.medical.cleanliness.server.statistics.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-01  11:47
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalDeviceStatistics extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 设备品类名称
     */
    private String deviceProdCategoryName;
    /**
     * 设备品类编码
     */
    private String deviceProdCategoryCode;
    /**
     * 设备总数
     */
    private Integer totalNumber;
    /**
     * 在线数量
     */
    private Integer normalNumber;
    /**
     * 离线数量
     */
    private Integer offlineNumber;
    /**
     * 采集时间
     */
    private LocalDateTime acquisitionDate;

}
