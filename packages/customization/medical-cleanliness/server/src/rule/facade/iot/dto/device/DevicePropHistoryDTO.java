package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.dto.device;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DevicePropHistoryDTO {

    /**
     * 属性code
     */
    private String key;

    /**
     * 属性值
     */
    private String value;

    /**
     * 上报时间
     */
    private LocalDateTime uploadTime;
}
