package com.guhecloud.cube.biz.ouaa.server.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-10 19:58
 */
@Data
public class PositionExcelDTO {
    @ExcelProperty("职位名称")
    private String name;
    @ExcelProperty("备注")
    private String remark;
    @ExcelIgnore
    private Boolean enabled;
    @ExcelProperty("职位状态")
    private String enabledText;

}
