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
public class MaintenanceRuleCondition extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * '预案主键'
     */
    private Long emergencyRuleId;
    /**
     * 设备产品品类编号
     */
    private String deviceProdCategoryCode;
    /**
     * '设备产品品类编号序列号'
     */
    private String deviceProdCategoryCodeConcat;
    /**
     * '设备产品品类名称'
     */
    private String deviceProdCategoryName;
    /**
     * '设备产品主键'
     */
    private Long deviceProdId;
    /**
     * 设备产品名称
     */
    private String deviceProdName;
    /**
     * 条件类型(属性，事件)
     */
    private String conditionType;
    /**
     * 事件主键
     */
    private String deviceEventId;
    /**
     * 事件编号
     */
    private String deviceEventCode;
    /**
     * 事件名称
     */
    private String deviceEventName;
    /**
     * 事件类型
     */
    private String deviceEventType;
    /**
     * 逻辑运算符
     */
    private String logicalOp;
    /**
     * 条件组主键
     */
    private Long groupId;
}
