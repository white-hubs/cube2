package com.guhecloud.cube.biz.mdata.server.purchaser.dto;

import lombok.Data;

/**
 * @author lzy
 * @date 2022/1/5
 */
@Data
public class PurchaserDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
