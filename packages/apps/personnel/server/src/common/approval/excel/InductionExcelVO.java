package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/24
 * @description 入职统计excel
 */
@Data
public class InductionExcelVO {

    @ExcelField(value = "入职岗位")
    private String postName;

    @ExcelField(value = "入职部门")
    private String organizationName;

    @ExcelField(value = "入职人数")
    private int num;

}