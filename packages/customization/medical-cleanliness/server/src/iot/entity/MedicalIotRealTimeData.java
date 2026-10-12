package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-21  14:50
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalIotRealTimeData extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 企业名称
     */
    private String tenantName;
    /**
     * 所属部门
     */
    private Long depId;
    private String depName;
    /**
     * 设备id
     */
    private Long deviceId;
    private String deviceName;
    /**
     * 指标状态
     */
    private String status;
    /**
     * 指标类型
     */
    private String normType;

}
