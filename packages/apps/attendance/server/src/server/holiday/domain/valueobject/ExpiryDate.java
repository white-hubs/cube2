package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author pengjian
 * @date 2022/11/21 11:29
 * @description 有效期
 */
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpiryDate {
    private ExpiryDateType expiryDateType;
    private Integer value;
    private TimeUnit timeUnit;

    /**
     * 失效时间单位
     */
    public enum TimeUnit {
        YEAR("年"),
        MONTH("月"),
        DAY("日");
        @Getter
        private final String value;

        TimeUnit(String value) {this.value = value;}
    }

    public enum ExpiryDateType {
        /**
         * 一直有效
         */
        ALONG("长期有效"),
        /**
         * 自然年
         */
        CALENDAR_YEAR("发放日起多少自然年内有效"),

        /**
         * 发放日后多少时间有效
         */
        FULL("发放日起多长时间内有效");
        @Getter
        private final String value;

        ExpiryDateType(String value) {this.value = value;}
    }
}
