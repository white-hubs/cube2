package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskPaperModeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试任务
 *
 * @author lanchun
 * @date 2023/4/19 11:47
 */
@Data
public class ExamTaskStandardVO {

    private Long id;

    /**
     * 考试任务名称
     */
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    private String themeName;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 试卷内容(整个大json信息)
     */
    private String examPaperContent;

    /**
     * 负责人主键
     */
    private Long chargeUserId;

    /**
     * 负责人名称
     */
    private String chargeUserName;

    /**
     * 是否限时
     */
    private Boolean timed;

    /**
     * 考试时长
     */
    private Integer examDuration;

    /**
     * 考试时长单位
     */
    private String examDurationUnit;

    /**
     * 是否支持复考
     */
    private Boolean retake;

    /**
     * 复考次数
     */
    private Integer retakeNumber;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 组卷方式：统一题目 / 随机出题
     */
    private ExamTaskPaperModeEnum examPaperMode;

    /**
     * 组卷方式名称
     */
    private String examPaperModeName;

    /**
     * 考试类型名称
     */
    private String examTaskTypeName;

    /**
     * 参加考试人数
     */
    private Integer examUserNumber;

    /**
     * 任务状态
     */
    private ExamTaskStatusEnum examTaskStatus;
    /**
     * 任务状态
     */
    private String examTaskStatusName;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 考试试卷信息
     */
    private List<ExamPaperVO> examPapers;

    /**
     * 考试说明
     */
    private String remark;

    private Integer tenant;

    private Long deleted;

    private String createBy;
    private String creatorId;
    private ActorTypeEnum creatorType;

    private LocalDateTime createTime;

    private String updateBy;

    private LocalDateTime updateTime;
}
