package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.interfaces.vo.ExamPaperVO;
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
public class HeadquartersExamTaskVO {

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
     * 考试说明
     */
    private String remark;

    /**
     * 考试市场信息
     */
    private List<HeadquartersExamTaskMarketVO> examTaskMarkets;

    /**
     * 考试试卷信息
     */
    private List<ExamPaperVO> examPapers;
}
