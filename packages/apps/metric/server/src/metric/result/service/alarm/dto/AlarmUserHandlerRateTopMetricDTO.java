package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmUserHandlerRateTopMetricDTO {

    /**
     * 总报警数
     */
    private Long totalAlarmNumber;

    /**
     * 已处理数
     */
    private Long handlerAlarmNumber;

    /**
     * 处理率
     */
    private Double handlerRate;

    /**
     * 处理率最高用户
     */
    private List<UserDetails> topHandlerUsers;

    /**
     * 处理率最低用户
     */
    private List<UserDetails> lowHandlerUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserDetails {

        /**
         * 用户处理报警数
         */
        private Integer userHandlerAlarmNumber;

        /**
         * 用户名称
         */
        private String alarmHandlerUserName;

        /**
         * 用户处理率
         */
        private Double userHandlerRate;
    }
}
