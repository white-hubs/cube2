package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/21 11:36
 * @description 假期发放规则
 */
@Getter
@AllArgsConstructor
public class HolidayGrantRule {

    /**
     * 发放方式
     */
    private GrantWay grantWay;

    /**
     * 发放时间
     */
    private GrantTime grantTime;

    /**
     * 假期额度发放额度
     */
    private List<HolidayGrantQuota> holidayGrantQuotas;

    /**
     * 发放方式
     */
    public enum GrantWay {
        /**
         * 每年自动发放一次
         */
        AUTO_OF_ANNUALLY("每年自动发放一次");
        @Getter
        private final String value;

        GrantWay(String value) {this.value = value;}
    }

    /**
     * 发放时间
     */
    public enum GrantTime {
        /**
         * 员工入职时期
         */
        INDUCTION("员工入职时期"),

        /**
         * 每年1月1日
         */
        JANUARY_FIRST_OF_ANNUALLY("每年1月1日");

        /**
         * 立即(手动初始化的时候)
         */
        @Getter
        private final String value;

        GrantTime(String value) {this.value = value;}
    }

    public enum GrantTimeUnit {
        DAY,
        HOUR
    }
}
