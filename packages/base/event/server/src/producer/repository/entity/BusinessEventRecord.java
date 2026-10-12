package com.guhecloud.cube.base.event.server.producer.repository.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "event_record")
public class BusinessEventRecord {

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
     * 订阅主题
     */
    private String topic;

    /**
     * 业务事件触发时间戳
     */
    private Long timestamp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
