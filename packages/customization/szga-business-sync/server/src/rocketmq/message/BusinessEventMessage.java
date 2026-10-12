package com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message;

import lombok.Data;

/**
 * @author lanchun
 * @date 2024/12/17 14:49
 */
@Data
public class BusinessEventMessage {

    public static final String TOPIC = "BUSINESS_DATA_EVENT_TRIGGER";

    /**
     * 事件数据包
     */
    private String data;

    /**
     * 业务事件类型
     */
    private String businessEventType;

    /**
     * 订阅主题
     */
    private String topic;

    /**
     * 业务事件触发时间戳
     */
    private Long timestamp;

}
