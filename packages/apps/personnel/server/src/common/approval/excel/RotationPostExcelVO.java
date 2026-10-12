package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/28
 * @description 轮岗统计excel
 */
@Data
public class RotationPostExcelVO {

    @ExcelField(value = "拟轮岗部门")
    private String rotationOrganizationName;

    @ExcelField(value = "现所在部门")
    private String organizationName;

    @ExcelField(value = "轮岗人数")
    private int num;

}