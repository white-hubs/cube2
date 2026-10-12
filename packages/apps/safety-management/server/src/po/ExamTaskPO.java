package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 考试任务
 *
 * @author lanchun
 * @date 2023/4/19 11:47
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName(value = "safety_exam_task")
public class ExamTaskPO extends BasicEnhancedTenantEntity<Long> {

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
    private String examTaskType;

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
    private String examTaskStatus;

    /**
     * 任务状态名称
     */
    private String examTaskStatusName;

    /**
     * 发布时间
     */
    private LocalDateTime publishedTime;

    /**
     * 考试说明
     */
    private String remark;

    private Integer tenant;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 市场名称
     */
    private String marketName;
}
