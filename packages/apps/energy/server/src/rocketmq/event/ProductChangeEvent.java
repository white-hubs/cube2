package com.guhecloud.cube.srv.gen.energymanagement.server.rocketmq.event;

import lombok.Data;
import lombok.ToString;

/**
 * 产品变更事件对象
 */
@Data
@ToString
public class ProductChangeEvent {
    private Long productId;
    private String newBrandName;
}
