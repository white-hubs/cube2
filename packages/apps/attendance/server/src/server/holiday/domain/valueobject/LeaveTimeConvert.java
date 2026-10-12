package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 11:12
 * @description 请假时长折算
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeaveTimeConvert {
    /**
     * 折算方式
     */
    @Getter
    private ConvertWay convertWay;

    /**
     * 返回折算后的时长
     *
     * @return 单位:小时
     */
    public Float convert(Float time) {
        return time;
    }

    /**
     * 折算方式
     */
    public enum ConvertWay {
        /**
         * 工作日
         */
        WORKING_DAY("工作日"),
        /**
         * 自然日
         */
        CALENDAR_DAY("自然日");
        @Getter
        private final String value;

        ConvertWay(String value) {
            this.value = value;
        }
    }
}
