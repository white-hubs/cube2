package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/27
 * @description 转正统计excel
 */
@Data
public class RegularWorkerExcelVO {

    @ExcelField(value = "所在部门")
    private String organizationName;

    @ExcelField(value = "所在岗位")
    private String postName;

    @ExcelField(value = "转正人数")
    private int num;

}