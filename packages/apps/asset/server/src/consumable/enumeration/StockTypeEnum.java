package com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration;

import lombok.Getter;

/**
 * 处理方式
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum StockTypeEnum {
    ADD("入库"),
    REDUCE("出库");
    private final String desc;

    StockTypeEnum(String desc) {
        this.desc = desc;
    }
}
