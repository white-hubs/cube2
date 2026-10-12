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
public class AlarmUserHandlerEfficiencyTopMetricDTO {

    /**
     * 处理效率最高用户
     */
    private List<UserDetails> topHandlerUsers;

    /**
     * 处理效率最低用户
     */
    private List<UserDetails> lowHandlerUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserDetails {

        /**
         * 用户名称
         */
        private String alarmHandlerUserName;

        /**
         * 平均耗时
         */
        private Double userAvgHandlerDuration;
    }
}
