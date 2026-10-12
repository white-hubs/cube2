package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.application.commandgateway.command;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 考试任务新增参数
 *
 * @author lanchun
 * @date 2023/4/19 15:01
 */
@Data
public class ChangeHeadquartersExamTaskCommand {

    private Long id;

    /**
     * 名称
     */
    @NotEmpty(message = "任务名称不能为空")
    @Size(max = 20, message = "名称不能大于20")
    private String name;

    /**
     * 考试主题主键
     */
    @NotNull(message = "考试主题主键不能为空")
    private Long themeId;

    /**
     * 考试开始时间
     */
    @NotNull(message = "考试开始时间不能为空")
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    @NotNull(message = "考试结束时间不能为空")
    private LocalDateTime examEndTime;

    /**
     * 试卷主键集合
     */
    @NotNull(message = "试卷不能为空")
    private Set<Long> examPaperIds;

    /**
     * 负责人主键
     */
    @NotNull(message = "负责人不能为空")
    private Long chargeUserId;

    /**
     * 是否限时
     */
    @NotNull(message = "是否限时不能为空")
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
    @NotNull(message = "是否支持复考不能为空")
    private Boolean retake;

    /**
     * 复考次数
     */
    private Integer retakeNumber;

    /**
     * 考试类型
     */
    @NotNull(message = "考试类型不能为空")
    private ExamTaskTypeEnum examTaskType;

    /**
     * 考试说明
     */
    @Size(max = 200, message = "考试说明不能大于200")
    private String remark;

    /**
     * 考试人员总数
     */
    private Integer examUserNumber;

    /**
     * 考试市场
     */
    @NotNull(message = "考试市场不能为空")
    @Size(min = 1, message = "考试市场数量不能小于1")
    private List<ChangeHeadquartersExamTaskMarketCommand> examTaskMarkets;
}
