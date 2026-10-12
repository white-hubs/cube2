package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DingTalkScheduleListByUsersResponse {

    /**
     * 排班ID。
     */
    private Long id;

    /**
     * 考勤类型：
     * Onduty：上班打卡
     * OffDuty：下班打卡
     */
    private String checkType;

    /**
     * 计划打卡时间。
     */
    private LocalDateTime planCheckTime;

    /**
     * 考勤组ID。
     */
    private Long groupId;

    /**
     * 用户userId。
     */
    private String userid;

    /**
     * 排班绑定的审批单ID。
     */
    private Long approveId;

    /**
     * 工作日，代表具体哪一天的排班。
     */
    private LocalDateTime workDate;

    /**
     * 班次版本。
     */
    private Long shiftVersion;

    /**
     * 班次ID，该值为0，表明当天休息。
     */
    private Long shiftId;

    /**
     * 是否休息
     * Y：当天排休
     * N：当天不休息
     */
    private String isRest;
}
