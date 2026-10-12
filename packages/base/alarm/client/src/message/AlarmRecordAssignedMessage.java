package com.guhecloud.cube.base.alarm.client.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 报警初始化指派完成消息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmRecordAssignedMessage {

    public static final String TOPIC = "ALARM_RECORD_ASSIGNED";

    /**
     * 租户编号。
     */
    private Integer tenant;

    /**
     * 报警记录主键。
     */
    private Long alarmRecordId;

    /**
     * 报警编号。
     */
    private String code;

    /**
     * 应用编号。
     */
    private String applicationCode;

    /**
     * 应用名称。
     */
    private String applicationName;

    /**
     * 处理用户。
     */
    private List<UserMessage> handlerUsers;

    /**
     * 报警通知人。
     */
    private List<UserMessage> notifyUsers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserMessage {

        private Long userId;

        private String userName;

        private Long departmentId;

        private String departmentName;
    }
}
