package com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 用户考试记录
 *
 * @author lanchun
 * @date 2023/4/19 14:48
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName(value = "exam_task_user_record")
public class ExamTaskUserRecordPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 存储提交的试卷内容json
     */
    private String examPaperContent;

    /**
     * 用户唯一标识
     */
    private String userIdentify;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户部门主键
     */
    private Long userDepartmentId;

    /**
     * 用户部门名称
     */
    private String userDepartmentName;

    /**
     * 用户岗位
     */
    private Long userPostId;

    /**
     * 用户岗位名称
     */
    private String userPostName;

    /**
     * 是否提交
     */
    private Boolean submitted;

    /**
     * 是否通过考试
     */
    private Boolean passed;

    /**
     * 的分数(目前取最高得分)
     */
    private Float score;

    /**
     * 考试时间
     */
    private LocalDateTime examTime;

    /**
     * 考试提交时间
     */
    private LocalDateTime examSubmittedTime;

    /**
     * 考试耗时(已秒为维度记录)
     */
    private Integer examDuration;
}
