package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

@Data
public class OverdueVO {
    /**
     * 资产总数
     */
    private Integer propertyNumber;
    /**
     * 报废资产数
     */
    private Integer weedOutNumber;
    /**
     * 使用年限到期资产
     */
    private Integer usageNumber;
    /**
     * 维保到期资产
     */
    private Integer maintenanceNumber = 0;
    /**
     * 报修资产数
     */
    private Integer repairReportNumber = 0;
    /**
     * 耗材总数
     */
    private Integer consumableNumber;
}
