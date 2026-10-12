package com.guhecloud.cube.srv.gen.official.management.server.querygateway.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author appet
 * @date 2023/02/24
 * @description 督办事项业务表
 */
@Data
public class FocusWorkExcelDTO {
    @ExcelProperty(value = "类别")
    private String workDesc;
    @ExcelProperty(value = "完成内容")
    private String requirement;
    @ExcelProperty(value = "牵头领导")
    private String leadLeaderUserName;
    @ExcelProperty(value = "负责部门")
    private String deptName;
    @ExcelProperty(value = "落实情况")
    private String practicableDetails;
    @ExcelProperty(value = "完成时间")
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(20)
    private LocalDateTime completeTime;
}