package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskStatusEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 总部考试任务
 *
 * @author lanchun
 * @date 2024/4/9 16:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "headquarters_exam_task")
public class HeadquartersExamTaskPO extends BasicEnhancedTenantEntity<Long> {

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
     * 参加考试人数
     */
    private Integer examUserNumber;

    /**
     * 任务状态
     */
    private ExamTaskStatusEnum examTaskStatus;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 考试说明
     */
    private String remark;
}
