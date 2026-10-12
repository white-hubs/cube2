package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskParam {

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
     * 培训人数
     */
    private Integer cultivateUserNumber;
    /**
     * 培训形式(线上、线下)
     */
    private String cultivateMode;
    /**
     * 培训对象(商户、员工)
     */
    private String cultivateTarget;
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

    /**
     * 迟到状态 (true代表关闭迟到状态)
     */
    private Boolean lateStatus;

    /**
     * 任务来源
     */
    private CultivateTaskSourceParam cultivateTaskSource;

    /**
     * 培训人员
     */
    private List<CultivateTaskUserParam> cultivateUsers;
    /**
     * 培训课件
     */
    private List<CultivateCoursewarParam> cultivateCoursewares;

    /**
     * 市场租户编号(当使用方为集团时可用)
     */
    private Integer marketTenant;
}