package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户考试记录返回参数
 *
 * @author lanchun
 * @date 2024/4/11 15:23
 */
@Data
public class ExamTaskUserRecordVO {

    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 部门名称
     */
    private String departmentName;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 市场信息
     */
    private ExamMarketInfoVO market;

    /**
     * 用户试卷信息
     */
    private ExamUserPaperInfoVO userExamPaper;

    /**
     * 用户考试时间
     */
    private ExamTimeInfoVO userExamTime;

    /**
     * 任务信息
     */
    private ExamTaskInfoVO examTask;

    /**
     * 任务试卷信息
     */
    private ExamPaperInfoVO examPaper;

    /**
     * 任务时间
     */
    private ExamTimeInfoVO examTime;

    /**
     * 考试市场信息
     *
     * @author lanchun
     * @date 2024/4/11 15:58
     */
    @Data
    public static class ExamMarketInfoVO {

        /**
         * 租户编号
         */
        private Integer marketTenant;

        /**
         * 租户名称
         */
        private String marketName;
    }

    @Data
    public static class ExamTimeInfoVO {

        /**
         * 开始时间
         */
        private LocalDateTime startTime;

        /**
         * 结束时间
         */
        private LocalDateTime endTime;

        /**
         * 考试耗时(秒数)
         */
        private Integer duration;

        /**
         * 翻译后的考试时长
         */
        private String translateDurationName;
    }

    @Data
    public static class ExamPaperInfoVO {

        /**
         * 总分
         */
        private Float score;

        /**
         * 及格分
         */
        private Float gradeScore;

    }

    @Data
    public static class ExamUserPaperInfoVO {

        /**
         * 分数
         */
        private Float score;

        /**
         * 考试状态
         */
        private UserExamStatusEnum status;

        /**
         * 是否通过
         */
        private Boolean passed;
    }

    @Data
    public static class ExamTaskInfoVO {

        /**
         * 任务名称
         */
        private String name;

        /**
         * 来源主键
         */
        private String sourceKey;

        /**
         * 主题名称
         */
        private String themeName;

        /**
         * 负责人名称
         */
        private String chargeUserName;
    }
}
