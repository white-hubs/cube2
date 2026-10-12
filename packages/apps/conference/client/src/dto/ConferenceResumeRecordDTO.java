package com.guhecloud.cube.srv.gen.conferencemanage.client.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会议履职记录响应。
 */
@Data
public class ConferenceResumeRecordDTO {

    /**
     * 会议预约ID。
     */
    private Long bookId;

    /**
     * 会议预约时段记录ID。
     */
    private Long intervalId;

    /**
     * OUAA用户ID。
     */
    private Long userId;

    /**
     * 会议室名称。
     */
    private String conferenceName;

    /**
     * 会议主题。
     */
    private String conferenceTheme;

    /**
     * 会议时段。
     */
    private String timeInterval;

    /**
     * 会议日期时间。
     */
    private LocalDateTime appointmentTime;

    /**
     * 预约状态。
     */
    private String status;

    /**
     * 流程实例ID。
     */
    private String processInstanceId;
}
