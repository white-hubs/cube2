package com.guhecloud.cube.biz.mdata.server.supplier.entity;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/12/16
 */
@Data
public class SupplierStall {

    /**
     * 主键
     */
    private Long id;

    /**
     * 批发商主键
     */
    private Long supplierId;

    /**
     * 档位主键
     */
    private Long stallId;

}
