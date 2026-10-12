package com.guhecloud.cube.base.flow.rocketmq.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: liJie
 * @Date: 2024/12/17 19:01
 * @Description:
 */

@Data
public class ProcessEventStartMessage {

    /**
     * 时间触发流程发起
     */
    public static final String TOPIC = "FLOW_EVENT_START";

    private Long id;

    /**
     * 数据包
     */
    private String data;

    /**
     * 业务事件类型
     */
    private String businessEventType;

    /**
     * 业务事件触发时间戳
     */
    private Long timestamp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
