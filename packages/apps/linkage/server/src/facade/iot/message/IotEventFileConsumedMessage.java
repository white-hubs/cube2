package com.guhecloud.cube.biz.linkage.server.facade.iot.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * iot事件附件成功消费消息
 *
 * @author lanchun
 * @date 2025/5/21 16:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IotEventFileConsumedMessage {

    public static final String TOPIC = "IOT_EVENT_FILE_CONSUMED";

    /**
     * 联动事件id
     */
    private Long eventId;

    /**
     * iot事件id
     */
    private String iotEventId;

    /**
     * 文件可访问路径
     */
    private String filePath;
}
