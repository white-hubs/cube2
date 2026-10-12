package com.guhecloud.cube.base.device.center.metadata.vo;

import lombok.Data;

/**
 * 产品品类统计在线离线数量返回参数
 *
 * @author lanchun
 * @date 2024/1/23 17:36
 */
@Data
public class ProductCategoryOffLineCountVO {

    /**
     * 品类编号
     */
    private String prodCategoryCode;

    /**
     * 品类编号序列号
     */
    private String prodCategoryConcat;

    /**
     * 品类名称
     */
    private String prodCategoryName;

    /**
     * 设备总数
     */
    private Integer totalNumber;

    /**
     * 在线数量
     */
    private int normalNumber;

    /**
     * 离线数量
     */
    private Integer offlineNumber;
}
