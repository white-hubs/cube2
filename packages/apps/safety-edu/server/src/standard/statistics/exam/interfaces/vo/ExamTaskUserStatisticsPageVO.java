package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import lombok.Data;

/**
 * 考试人数统计
 *
 * @author lanchun
 * @date 2023/4/21 11:07
 */
@Data
public class ExamTaskUserStatisticsPageVO {

    private Long id;

    /**
     * 考试任务主键
     */
    private Long examTaskId;

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
     * 用户考试记录主键(目前取最高得分记录)
     */
    private Long userExamTaskRecordId;

    /**
     * 考试状态
     */
    private UserExamStatusEnum examStatus;

    /**
     * 考试次数
     */
    private Integer examNumber;

    /**
     * 是否通过考试
     */
    private Boolean passed;

    /**
     * 的分数(目前取最高得分)
     */
    private Float score;
}
