package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/28
 * @description 离职统计excel
 */
@Data
public class LeaveOfficeExcelVO {

    @ExcelField(value = "离职岗位")
    private String postName;

    @ExcelField(value = "离职部门")
    private String organizationName;

    @ExcelField(value = "离职人数")
    private int num;

}