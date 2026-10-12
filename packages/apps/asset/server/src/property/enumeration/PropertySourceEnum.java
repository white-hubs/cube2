package com.guhecloud.cube.srv.gen.property.management.server.property.enumeration;

import lombok.Getter;

/**
 * 资产来源
 *
 * @author : lijie
 * @date : 2023-4-14
 */
@Getter
public enum PropertySourceEnum {
    FAIL("审核中、审核失败"),
    DEPOSITORY("已经入库"),
    WIN("添加盘赢");
    private final String remarks;

    PropertySourceEnum(String remarks) {
        this.remarks = remarks;
    }
}
