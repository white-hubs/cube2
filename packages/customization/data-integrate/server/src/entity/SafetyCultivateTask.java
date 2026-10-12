package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Appet
 * @Description 培训数据采集
 * @date 2023-09-12 15:08:36
 */
@Data
public class SafetyCultivateTask {

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
    //培训形式名称(线上,线下)
    private String cultivateModeName;
    //培训对象(商户、员工)
    private String cultivateTarget;
    //培训对象名称(商户，员工)
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
    //培训任务状态名称
    private String cultivateTaskStatusName;

    private String marketName;
    private Integer marketTenant;

    private Integer tenant;

    private String createBy;

    private String creatorId;

    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private String updaterId;

    private ActorTypeEnum updaterType;

    private LocalDateTime updateTime;

    private Long deleted;
}