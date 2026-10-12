package com.guhecloud.cube.biz.emergency.server.rule.infrastructure.consumer.message;

import lombok.Data;

@Data
public class ProductChangeEvent {

    /**
     * 产品id
     */
    private Long productId;
    /**
     * 品牌名称
     */
    private String newBrandName;
}
