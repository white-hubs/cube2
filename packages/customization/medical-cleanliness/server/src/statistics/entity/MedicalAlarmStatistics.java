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
public class MedicalAlarmStatistics extends BasicEnhancedTenantEntity<Long> {
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
    private Long totalNum;
    private Long unConfirmedNum;
    private Long progressConfirmedNum;
    private Long suspendNum;
    private Long handlerNum;
    private String deviceName;
    private String deviceCode;
    /**
     * 采集时间
     */
    private LocalDateTime acquisitionDate;
}
