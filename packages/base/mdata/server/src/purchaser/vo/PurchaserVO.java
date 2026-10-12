package com.guhecloud.cube.biz.mdata.server.purchaser.vo;

import lombok.Data;

/**
 * @author lzy
 * @date 2021/12/30
 */
@Data
public class PurchaserVO {

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

    /**
     * 市场名称
     */
    private String marketName;

}
