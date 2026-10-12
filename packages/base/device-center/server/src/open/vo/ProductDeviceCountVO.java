package com.guhecloud.cube.base.device.center.open.vo;

import lombok.Data;

/**
 * 产品统计设备数量返回参数
 *
 * @author lanchun
 * @date 2024/1/23 17:36
 */
@Data
public class ProductDeviceCountVO {

    /**
     * 产品id
     */
    private Long prodId;

    /**
     * 品类名称
     */
    private String prodName;

    /**
     * 设备总数
     */
    private Integer totalNumber;
}
