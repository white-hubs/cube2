package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskPageVO {

    //
    private Long id;
    //任务编号
    private String code;
    //任务名称
    private String name;
    //培训主题
    private Long themeId;
    //培训主题名称
    private String themeName;
    /**
     * 任务来源编号
     */
    private String sourceCode;
    /**
     * 任务来源名称
     */
    private String sourceName;
    /**
     * 任务来源键
     */
    private String sourceKey;
    //培训开始时间
    private LocalDateTime cultivateStartTime;
    //培训结束时间
    private LocalDateTime cultivateEndTime;
    //考试任务主键
    private Long examTaskId;
    //培训人数
    private Integer cultivateUserNumber;
    //培训形式(线上、线下)
    private String cultivateMode;
    private String cultivateModeName;
    //培训对象(商户、员工)
    private String cultivateTarget;
    private String cultivateTargetName;
    //培训地点
    private String cultivateAddress;
    //主讲人
    private String speaker;
    //负责人
    private Long chargeUserId;
    //负责人名称
    private String chargeUserName;
    //培训内容
    private String cultivateContent;
    //培训任务状态
    private String cultivateTaskStatus;
    private String cultivateTaskStatusName;
    private Integer tenant;
    private Long deleted;
    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
}