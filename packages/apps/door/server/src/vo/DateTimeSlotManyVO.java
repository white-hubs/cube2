package com.guhecloud.cube.biz.door.server.vo;

import lombok.Data;

/**
 * 两个时间维度
 */
@Data
public class DateTimeSlotManyVO {
    /**
     * 现在这个时间维度
     */
    private DateTimeSlotVO currentDate;
    /**
     * 上个时间维度
     */
    private DateTimeSlotVO previousDate;

    public DateTimeSlotManyVO(DateTimeSlotVO currentDate, DateTimeSlotVO previousDate) {
        this.currentDate = currentDate;
        this.previousDate = previousDate;
    }

}
