package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.tool;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatutoryHolidayResponse {
    private String date;
    private Status status;

    public enum Status {
        /*
         * 改为工作日的休息日
         */
        WORKING,
        /**
         * 节假日
         */
        DAY_OFF
    }
}
