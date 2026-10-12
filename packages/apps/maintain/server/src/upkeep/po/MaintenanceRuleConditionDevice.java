package com.guhecloud.cube.srv.gen.maintain.server.upkeep.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-19  15:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class MaintenanceRuleConditionDevice extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 预案规则主键
     */
    private Long emergencyRuleId;
    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;
    /**
     * 设备产品品类编号序列号
     */
    private String deviceProdCategoryCodeConcat;
    /**
     * 设备产品品类名称
     */
    private String deviceProdCategoryName;
    /**
     * 设备产品主键
     */
    private Long deviceProdId;
    /**
     * 设备产品名称
     */
    private String deviceProdName;
    /**
     * 设备主键
     */
    private Long deviceId;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备所属区域
     */
    private String deviceAreaId;
    /**
     * 设备所属区域名称
     */
    private String deviceAreaName;

}
