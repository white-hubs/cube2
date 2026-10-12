package com.guhecloud.cube.biz.metric.server.metric.result.service.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警处理超时率统计
 *
 * @author lanchun
 * @date 2025/6/20 16:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmHandlerTimeoutRateMetricDTO {

    /**
     * 报警总处理数
     */
    private Integer totalHandlerNum;

    /**
     * 按时处理数量
     */
    private Integer onTimeHandlerNum;

    /**
     * 报警总处理超时数量
     */
    private Integer totalHandlerTimeoutNum;

    /**
     * 按时处理占比
     */
    private Double onTimeHandlerRate;

    /**
     * 效率高用户列表
     */
    private List<Users> topUsers;

    /**
     * 效率低用户列表
     */
    private List<Users> lowUsers;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Users {

        /**
         * 用户id
         */
        private String userId;

        /**
         * 用户名称
         */
        private String username;

        /**
         * 报警总处理数
         */
        private Integer totalHandlerNum;

        /**
         * 按时处理数量
         */
        private Integer onTimeHandlerNum;

        /**
         * 按时处理占比
         */
        private Double onTimeHandlerRate;

        /**
         * 报警总处理超时数量
         */
        private Integer totalHandlerTimeoutNum;
    }
}
