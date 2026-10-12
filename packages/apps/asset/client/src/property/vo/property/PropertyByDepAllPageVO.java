package com.guhecloud.cube.property.management.client.property.vo.property;

import lombok.Data;

@Data
public class PropertyByDepAllPageVO {
    /**
     * 部门编码
     */
    private String depCode;
    /**
     * 部门名称
     */
    private Long depId;
    private String depName;
    /**
     * 资产数量
     */
    private Integer totalNumber;
    /**
     * 资产金额
     */
    private String totalPrice;
}
