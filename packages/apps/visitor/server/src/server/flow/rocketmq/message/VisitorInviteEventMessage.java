package com.guhecloud.cube.srv.gen.visitor.management.server.flow.rocketmq.message;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/2/13 16:04
 * @Description: 访客邀约事件触发消息
 */
@Data
public class VisitorInviteEventMessage {

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
