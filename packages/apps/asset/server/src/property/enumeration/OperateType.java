package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

/**
 * 操作类型
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum OperateType {
    DEPOSITORY("RK", "资产入库"),
    ALLOT_PERSONAL("LY", "资产派发"),
    ALLOT_UNIT("DB", "资产调拨"),
    WEED_OUT("BF", "清理报废"),
    CONSUMABLE_ADD("RK", "耗材入库"),
    CONSUMABLE_REDUCE("LY", "耗材入库"),
    PROPERTY_CODE("ZC", "资产编码"),

    ;
    private final String code;
    private final String remarks;

    OperateType(String code, String remarks) {
        this.code = code;
        this.remarks = remarks;
    }

    /**
     * 生成编号
     */
    public static String getNumbers(OperateType operateType) {
        return operateType.getCode() + System.currentTimeMillis();
    }
}
