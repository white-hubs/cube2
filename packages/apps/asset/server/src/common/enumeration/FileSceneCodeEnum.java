package com.guhecloud.cube.srv.gen.property.management.server.common.enumeration;

import lombok.Getter;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-18  15:50
 * @Description:
 * @Version: 1.0
 */
@Getter
public enum FileSceneCodeEnum {
    PROPERTY_MANAGEMENT_CONSUMABLE("PROPERTY_MANAGEMENT::CONSUMABLE", "资产管理_耗材管理"),
    PROPERTY_MANAGEMENT_CONSUMABLE_FLOW("PROPERTY_MANAGEMENT::CONSUMABLE::FLOW", "资产管理_流程审批"),
    PROPERTY_MANAGEMENT_PROPERTY("PROPERTY_MANAGEMENT::PROPERTY", "资产管理_资产清单"),
    PROPERTY_MANAGEMENT_PROPERTY_FLOW("PROPERTY_MANAGEMENT::PROPERTY::FLOW", "资产管理_流程审批"),
    RISK_SOURCE("RISK_SOURCE", "风险源管理"),

    ;
    private final String code;
    private final String remarks;

    FileSceneCodeEnum(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }
}
