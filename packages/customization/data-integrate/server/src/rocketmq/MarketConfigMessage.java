package com.guhecloud.cube.base.dataintegrate.server.rocketmq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketConfigMessage {
    /**
     * 消息主题
     */
    public static final String TOPIC = "market-config-init";

    private Long marketId;
}
