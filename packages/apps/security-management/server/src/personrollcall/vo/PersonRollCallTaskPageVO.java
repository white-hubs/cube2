package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 会见申请分页vo
 */
@Data
public class PersonRollCallTaskPageVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 点名任务编号
     */
    private String taskCode;

    /**
     * 点名任务名称
     */
    private String taskName;

    /**
     * 场所名称
     */
    private String locationName;

    /**
     * 点名类别
     */
    private String callType;

    /**
     * 点名开始时间
     */
    private LocalDateTime callStartTime;

    /**
     * 点名结束时间
     */
    private LocalDateTime callEndTime;

    /**
     * 状态（0已结束，1执行中，2待执行）
     */
    private Integer status;

    /**
     * 已点到人数
     */
    private Integer attendedCount;

    /**
     * 未点到人数
     */
    private Integer unattendedCount;

    /**
     * 特殊人数
     */
    private Integer specialCount;

    /**
     * 点名时长
     */
    private Integer duration;

    /**
     * 第三方记录id 任务记录id
     */
    private String thirdRecordId;
}
