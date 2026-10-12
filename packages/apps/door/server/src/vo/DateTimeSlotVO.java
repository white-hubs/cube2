package com.guhecloud.cube.biz.door.server.vo;

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

    public DateTimeSlotVO() {
    }

    public DateTimeSlotVO(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
