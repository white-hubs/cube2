package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/17
 * @description 考核统计excel
 */
@Data
public class AssessExcelVO {

    @ExcelField(value = "所在岗位")
    private String postName;

    private String organizationName;

    @ExcelField("被考核员工数")
    private int num;
}