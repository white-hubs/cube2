package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.producer.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GmTallyDetailMessage {

    /**
     * 挂钩号
     */
    private String hookNumber;

    /**
     * 脚标号
     */
    private String footNumber;

    /**
     * 重量
     */
    private BigDecimal weight;
}
