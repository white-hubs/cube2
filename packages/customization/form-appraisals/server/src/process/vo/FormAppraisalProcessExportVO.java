package com.guhecloud.cube.srv.gen.appraisals.server.process.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FormAppraisalProcessExportVO {
    @ExcelProperty("编号")
    private String formNo;

    @ExcelProperty("申请名称")
    private String instanceName;

    @ExcelProperty("发起人")
    private String startUserName;

    @ExcelProperty("发起部门")
    private String organizationName;

    @ExcelProperty("发起时间")
    private LocalDateTime submitedTime;

    @ExcelProperty("处理状态")
    private String statusDesc;

    @ExcelProperty("处理进度")
    private String activeNodeName;
}
