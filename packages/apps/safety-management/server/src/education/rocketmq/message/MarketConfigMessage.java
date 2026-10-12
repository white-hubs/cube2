package com.guhecloud.cube.srv.gen.safety.management.education.rocketmq.message;

import lombok.Data;

@Data
public class MarketConfigMessage {

    public static final String TOPIC = "market-config-init";

    /**
     * 市场主键
     */
    private Long marketId;
}
