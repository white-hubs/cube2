package com.guhecloud.cube.srv.gen.safety.education.server.standard.statistics.exam.interfaces.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.exam.domain.enumeration.UserExamStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 考试人数统计导出
 *
 * @author lanchun
 * @date 2023/4/21 11:07
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(21)
@HeadRowHeight(30)
@ColumnWidth(20)
@ExcelIgnoreUnannotated
public class ExamTaskUserStatisticsPageExportVO {

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
    @ExcelProperty(value = { "员工名称" }, index = 0)
    private String userName;

    /**
     * 用户电话
     */
    @ExcelProperty(value = { "用户电话" }, index = 1)
    private String userPhone;

    /**
     * 用户部门主键
     */
    private Long userDepartmentId;

    /**
     * 用户部门名称
     */
    @ExcelProperty(value = { "部门" }, index = 2)
    private String userDepartmentName;

    /**
     * 用户岗位
     */
    private Long userPostId;

    /**
     * 用户岗位名称
     */
    @ExcelProperty(value = { "岗位" }, index = 3)
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
    @ExcelProperty(value = { "考试分数" }, index = 4)
    private Float score;
}
