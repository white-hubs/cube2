package com.guhecloud.cube.srv.gen.maintain.server.upkeep.rocketmq.consumer.message;

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
