package com.guhecloud.cube.srv.gen.appraisals.server.record.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FormAppraisalRecordExportVO {
    @ExcelProperty("一级分组")
    private String firstLevelFormGroupName;

    @ExcelProperty("二级分组")
    private String secondLevelFormGroupName;

    @ExcelProperty("规则名称")
    private String name;

    @ExcelProperty("检查项目")
    private String inspectionItem;

    @ExcelProperty("年分")
    private Integer assessmentYear;

    @ExcelProperty("周期类型")
    private String periodTypeText;

    @ExcelProperty("责任部门")
    private String responsibleDepartmentNames;

    @ExcelProperty("原始总分")
    private BigDecimal originTotalScore;

    @ExcelProperty("最终总分")
    private BigDecimal finalTotalScore;

    @ExcelProperty("扣分")
    private BigDecimal deductScore;

    @ExcelProperty("考核开始时间")
    private LocalDateTime assessmentBeginTime;

    @ExcelProperty("考核结束时间")
    private LocalDateTime assessmentEndTime;

    @ExcelProperty("生成时间")
    private LocalDateTime generatedAt;
}
