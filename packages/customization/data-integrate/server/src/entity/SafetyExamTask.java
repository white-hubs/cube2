package com.guhecloud.cube.base.dataintegrate.server.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Appet
 * @Description 考试数据采集
 * @date 2023-09-12 15:08:36
 */
@Data
public class SafetyExamTask {

    //
    private Long id;
    //考试任务名称
    private String name;
    //主题主键
    private Long themeId;
    //主题名称
    private String themeName;
    //考试开始时间
    private LocalDateTime examStartTime;
    //考试结束时间
    private LocalDateTime examEndTime;
    //试卷主键
    private Long examPaperId;
    //试卷名称
    private String examPaperName;
    //试卷内容(整个大json信息)
    private String examPaperContent;
    //负责人主键
    private Long chargeUserId;
    //负责人名称
    private String chargeUserName;
    //是否限时
    private Boolean timed;
    //考试时长
    private Integer examDuration;
    //考试时长单位
    private String examDurationUnit;
    //是否支持复考
    private Boolean retake;
    //复考次数
    private Integer retakeNumber;
    //考试类型
    private String examTaskType;
    //考试类型名称
    private String examTaskTypeName;
    //参加考试人数
    private Integer examUserNumber;
    //任务状态
    private String examTaskStatus;
    //任务状态名称
    private String examTaskStatusName;
    //发布时间
    private LocalDateTime publishedTime;
    //考试说明
    private String remark;

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

    @TableField(exist = false)
    private List<ExamPaper> examPapers;

    public record ExamPaper(Long id, String name) {}

}