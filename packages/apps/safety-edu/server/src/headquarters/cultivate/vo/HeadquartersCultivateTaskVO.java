package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 培训任务
 *
 * @author lanchun
 * @date 2024/4/10 14:24
 */
@Data
public class HeadquartersCultivateTaskVO {
    private Long id;
    /**
     * 任务编号
     */
    private String code;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 培训主题
     */
    private Long themeId;
    /**
     * 培训主题名称
     */
    private String themeName;
    /**
     * 培训开始时间
     */
    private LocalDateTime cultivateStartTime;
    /**
     * 培训结束时间
     */
    private LocalDateTime cultivateEndTime;
    /**
     * 考试任务主键
     */
    private Long examTaskId;
    /**
     * 考试任务名称
     */
    private String examTaskName;
    /**
     * 培训人数
     */
    private Integer cultivateUserNumber;
    /**
     * 培训形式(线上、线下)
     */
    private String cultivateMode;
    private String cultivateModeName;
    /**
     * 培训对象(商户、员工)
     */
    private String cultivateTarget;
    private String cultivateTargetName;
    /**
     * 培训地点
     */
    private String cultivateAddress;
    /**
     * 主讲人
     */
    private String speaker;
    /**
     * 负责人
     */
    private Long chargeUserId;
    /**
     * 负责人名称
     */
    private String chargeUserName;
    /**
     * 培训内容
     */
    private String cultivateContent;
    /**
     * 培训任务状态
     */
    private String cultivateTaskStatus;
    private String cultivateTaskStatusName;

}