package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 15:53
 * @Description:
 */
@Data
public class BusinessEventMessage {
    /**
     * 时间触发流程发起
     */
    public static final String START_TOPIC = "EVENT_START";

    /**
     * 事件处理
     */
    public static final String PASS_TOPIC = "EVENT_PASS";

    /**
     * 事件退回
     */
    public static final String BACK_BACK = "EVENT_BACK";

    /**
     * 事件完成
     */
    public static final String COMPLETE_TOPIC = "EVENT_COMPLETE";

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
