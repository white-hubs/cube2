package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-22  14:06
 * @Description:
 * @Version: 1.0
 */
@Data
public class MedicalIotRealTimeDataDetails extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    private Long deviceId;
    private String deviceName;
    private BigDecimal value;
    /**
     * 指标状态
     */
    private String status;
    /**
     * 指标编码
     */
    private String ruleNormCode;
    private String ruleNormName;
    /**
     * 单位
     */
    private String unit;
    /**
     * 指标类型
     */
    private String type;
}
