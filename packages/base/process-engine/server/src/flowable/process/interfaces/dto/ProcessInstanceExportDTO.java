package com.guhecloud.cube.base.flow.flowable.process.interfaces.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/9 10:38
 * @Description:
 */
@Data
public class ProcessInstanceExportDTO implements Serializable {

    @ExcelProperty(value = "编号")
    private String formNo;
    @ExcelProperty(value = "申请名称")
    private String instanceName;
    @ExcelProperty(value = "发起人")
    private String startUserName;
    @ExcelProperty(value = "发起部门")
    private String organizationName;
    @ExcelProperty(value = "发起时间")
    private LocalDateTime submitedTime;

    @ExcelProperty(value = "处理状态")
    private String statusDesc;
    @ExcelProperty(value = "处理进度")
    private String activeNodeName;
}
