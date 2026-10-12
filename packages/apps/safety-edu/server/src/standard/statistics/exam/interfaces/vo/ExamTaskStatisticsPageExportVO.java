package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.ExamTaskTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 考核任务分页统计导出参数
 *
 * @author lanchun
 * @date 2023/4/20 17:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class ExamTaskStatisticsPageExportVO {

    private Long id;

    /**
     * 考试任务名称
     */
    @ExcelProperty(value = { "任务名称" }, index = 0)
    private String name;

    /**
     * 主题主键
     */
    private Long themeId;

    /**
     * 主题名称
     */
    @ExcelProperty(value = { "考试主题" }, index = 1)
    private String themeName;

    /**
     * 考试类型
     */
    private ExamTaskTypeEnum examTaskType;

    /**
     * 考试类型名称
     */
    @ExcelProperty(value = { "考试类型" }, index = 2)
    private String examTaskTypeName;

    /**
     * 负责人主键
     */
    private Long chargeUserId;

    /**
     * 负责人名称
     */
    @ExcelProperty(value = { "负责人" }, index = 3)
    private String chargeUserName;

    /**
     * 考试开始时间
     */
    private LocalDateTime examStartTime;

    /**
     * 考试结束时间
     */
    private LocalDateTime examEndTime;

    /**
     * 考试时间
     */
    @ExcelProperty(value = { "考试时间" }, index = 4)
    private String examTime;

    /**
     * 试卷主键
     */
    private Long examPaperId;

    /**
     * 试卷名称
     */
    @ExcelProperty(value = { "试卷" }, index = 5)
    private String examPaperName;

    /**
     * 试卷总分
     */
    @ExcelProperty(value = { "总分" }, index = 6)
    private Float examPaperScore;

    /**
     * 预计考试人数
     */
    @ExcelProperty(value = { "预计参加人数" }, index = 7)
    private Integer examUserNumber;

    /**
     * 实际考试人数
     */
    @ExcelProperty(value = { "实际参加人数" }, index = 8)
    private Integer realityExamUserNumber;

    /**
     * 缺考人数
     */
    @ExcelProperty(value = { "缺考人数" }, index = 9)
    private Integer missExamUserNumber;

    /**
     * 不及格人数
     */
    @ExcelProperty(value = { "不及格人数" }, index = 10)
    private Integer noPassedUserNumber;

    /**
     * 平均分
     */
    @ExcelProperty(value = { "平均分" }, index = 11)
    private Float averageScore;

    /**
     * 总分
     */
    private Float totalScore;

    /**
     * 及格率
     */
    @ExcelProperty(value = { "及格率" }, index = 12)
    private String passingRate;

    /**
     * 考试说明
     */
    private String remark;
}
