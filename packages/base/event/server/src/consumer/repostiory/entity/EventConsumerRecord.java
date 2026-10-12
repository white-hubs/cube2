package com.guhecloud.cube.base.event.server.consumer.repostiory.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "event_consumer_record")
public class EventConsumerRecord {

    private Long id;

    /**
     * 事件id
     */
    private Long eventId;

    /**
     * 时间戳
     */
    private Long timestamp;
}
