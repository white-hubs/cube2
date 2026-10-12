package com.guhecloud.cube.property.management.client.common.vo;

import lombok.Data;

/**
 * 时间维度的开始、结束时间
 */
@Data
public class DateTimeSlotStrVO {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public DateTimeSlotStrVO() {
    }

    public DateTimeSlotStrVO(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
