package com.guhecloud.cube.srv.gen.securitymanagement.server.common.rocketmq.message;

import lombok.Data;

@Data
public class ProductChangeEvent {
    private Long productId;
    private String newBrandName;
}
