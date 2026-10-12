package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 考核任务分页统计返回参数
 *
 * @author lanchun
 * @date 2023/4/20 17:37
 */
@Data
public class ExamTaskStatisticsPageVO {

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
     * 试卷名称
     */
    private String examPaperName;

    /**
     * 负责人主键
     */
    private Long chargeUserId;

    /**
     * 负责人名称
     */
    private String chargeUserName;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 试卷总分
     */
    private Float examPaperScore;

    /**
     * 考试类型名称
     */
    private String examTaskTypeName;

    /**
     * 预计考试人数
     */
    private Integer examUserNumber;

    /**
     * 实际考试人数
     */
    private Integer realityExamUserNumber;

    /**
     * 缺考人数
     */
    private Integer missExamUserNumber;

    /**
     * 不及格人数
     */
    private Integer noPassedUserNumber;

    /**
     * 平均分
     */
    private Float averageScore;

    /**
     * 总分
     */
    private Float totalScore;

    /**
     * 及格率
     */
    private Float passingRate;

    /**
     * 考试说明
     */
    private String remark;

    public Float getAverageScore() {
        if (Objects.isNull(this.realityExamUserNumber)) {
            return null;
        }
        if (0 < this.realityExamUserNumber) {
            double localAverageScore = totalScore / realityExamUserNumber;
            return BigDecimal.valueOf(localAverageScore).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return 0f;
    }

    public Float getPassingRate() {
        if (Objects.isNull(this.examUserNumber)) {
            return null;
        }
        if (0 < this.examUserNumber) {
            //及格人数
            Integer passedUserNumber = this.realityExamUserNumber - noPassedUserNumber;
            return new BigDecimal(passedUserNumber)
                    .divide(new BigDecimal(examUserNumber), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal(100)).floatValue();
        }
        return 0f;
    }

    public String getExamTaskTypeName() {
        return this.examTaskType.getDescription();
    }
}
