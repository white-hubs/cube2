package com.guhecloud.cube.srv.gen.personnel.management.server.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-10 20:40
 */
@Data
public class UserExcelDTO {
    /**
     * 类别名称
     */
    @ExcelProperty("人员类别")
    private String typeName;
    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String name;
    /**
     * 联系电话
     */
    @ExcelProperty("联系电话")
    private String phone;

}
