package com.guhecloud.cube.srv.gen.property.management.server.consumable.enumeration;

import lombok.Getter;

/**
 * 出入库的业务来源
 *
 * @author : appet
 * @date : 2023-3-16
 */
@Getter
public enum BusinessTypeEnum {
    APPROVE("审批"),
    CHECK("盘点"),
    IMPORT("导入");
    private final String desc;

    BusinessTypeEnum(String desc) {
        this.desc = desc;
    }
}
