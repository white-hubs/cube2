package com.guhecloud.cube.iot.mgt.things.vo;


import lombok.Data;

@Data
public class ProductChangeEvent {
    private Long productId;

    /**
     * 品牌
     */
    private String newBrandName;

    /**
     * 创建产品变更事件
     *
     * @param productId    产品ID
     * @param newBrandName 新品牌名称
     * @return 产品变更事件
     */
    public static ProductChangeEvent of(Long productId, String newBrandName) {
        ProductChangeEvent event = new ProductChangeEvent();
        event.setProductId(productId);
        event.setNewBrandName(newBrandName);
        return event;
    }
}
