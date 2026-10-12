package com.guhecloud.cube.property.management.client.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 时间维度的开始、结束时间
 */
@Data
public class DateTimeSlotVO {
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    private DateTimeSlotVO() {
    }

    public DateTimeSlotVO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
