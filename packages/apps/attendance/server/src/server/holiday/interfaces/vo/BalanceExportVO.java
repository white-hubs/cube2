package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.convert.BalanceExportConvert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BalanceExportVO {
    @ExcelProperty(value = "姓名")
    @ColumnWidth(18)
    private String employeeName;
    @ExcelProperty(value = "部门")
    @ColumnWidth(22)
    private String deptNam;
    @ExcelProperty(value = "入职时间")
    @DateTimeFormat("yyyy-MM-dd")
    @ColumnWidth(22)
    private LocalDateTime timeOfInduction;
    @ExcelProperty(value = "年假", converter = BalanceExportConvert.class)
    private String annualLeave;
    @ExcelProperty(value = "调休", converter = BalanceExportConvert.class)
    private String compensatoryLeave;
    @ExcelProperty(value = "事假", converter = BalanceExportConvert.class)
    private String compassionateLeave;
    @ExcelProperty(value = "病假", converter = BalanceExportConvert.class)
    private String sickLeave;
    @ExcelProperty(value = "产假", converter = BalanceExportConvert.class)
    private String maternityLeave;
    @ExcelProperty(value = "陪产假", converter = BalanceExportConvert.class)
    private String paternityLeave;
    @ExcelProperty(value = "婚假", converter = BalanceExportConvert.class)
    private String marriageLeave;
    @ExcelProperty(value = "丧假", converter = BalanceExportConvert.class)
    private String bereavementLeave;
}
