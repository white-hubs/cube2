package com.guhecloud.cube.base.event.server.consumer.publisher.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {

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
