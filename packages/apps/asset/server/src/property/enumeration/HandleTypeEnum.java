package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

/**
 * 处理方式
 *
 * @author : lijie
 * @date : 2023-3-16
 */
@Getter
public enum HandleTypeEnum {
    DEPOSITORY("资产入库"),
    DISTRIBUTION("资产派发"),
    OUTBOUND("资产调拨"),
    WEED_OUT("资产报废");
    private final String remarks;

    HandleTypeEnum(String remarks) {
        this.remarks = remarks;
    }
}
