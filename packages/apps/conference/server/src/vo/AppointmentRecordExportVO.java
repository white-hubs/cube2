package com.guhecloud.cube.srv.gen.conferencemanage.server.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/9/24 16:56
 * @Description: 会议室预约记录导出VO
 */

@Data
public class AppointmentRecordExportVO implements Serializable {
    @ExcelProperty(value = "姓名")
    private String userName;
    @ExcelProperty(value = "部门")
    private String departmentName;
    @ExcelProperty(value = "操作时间")
    private LocalDateTime createTime;
    @ExcelProperty(value = "操作")
    private String operateDesc;
    @ExcelProperty(value = "会议室名称")
    private String conferenceName;
    @ExcelProperty(value = "会议时间")
    private String timeInterval;
    @ExcelProperty(value = "会议物品登记")
    private String goodsNames;
    @ExcelProperty(value = "会议取消理由")
    private String cancelReason;
}
