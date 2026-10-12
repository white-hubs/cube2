package com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lzy
 * @date 2021/12/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDelayMessage {

    public static final String TOPIC = "ORDER-DELAY";

    /**
     * 订单主键
     */
    private Long orderId;
}
