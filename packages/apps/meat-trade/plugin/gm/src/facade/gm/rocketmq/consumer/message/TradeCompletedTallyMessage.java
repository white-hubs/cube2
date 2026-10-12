package com.guhecloud.cube.gm.meattrade.plugin.facade.gm.rocketmq.consumer.message;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TradeCompletedTallyMessage {

    /**
     * 上报时间
     */
    private LocalDateTime reportTime;

    private String appKey;

    /**
     * 挂钩信息
     */
    private List<Hook> details;

    /**
     * 挂钩信息
     */
    @Data
    public static class Hook {

        /**
         * 挂钩
         */
        private String hookNumber;

        /**
         * 角标
         */
        private String footNumber;
    }
}
