package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo;

import lombok.Data;

/**
 * @author Appet
 * @Description 培训任务
 * @date 2023-04-18 15:08:36
 */
@Data
public class CultivateTaskWithStatVO extends CultivateTaskVO {
    private int trainNum = 0;//培训人数
    private int signedNum = 0;//到勤人数
    private int unsignedNum = 0;//缺勤人数
    private int belatedNum = 0;//迟到人数
    private String hasExam;//是否有考试
    /**
     * 试卷总分
     */
    private Float examPaperScore;

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
     * 及格率
     */
    private Float passingRate;

}