package com.guhecloud.cube.biz.ouaa.server.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-10 19:58
 */
@Data
public class AppointmentExcelDTO {
    @ExcelProperty("岗位名称")
    private String name;
    @ExcelProperty("备注")
    private String remark;
    @ExcelIgnore
    private Boolean enable;
    @ExcelProperty("岗位状态")
    private String enabledText;

}
