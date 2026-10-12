package com.guhecloud.cube.srv.gen.personnel.management.server.common.approval.excel;

import com.guhecloud.cube.srv.gen.personnel.management.server.tool.excel.annotation.ExcelField;
import lombok.Data;

/**
 * @author bq
 * @date 2023/02/17
 * @description 招聘统计excel
 */
@Data
public class RecruitExcelVO {

    @ExcelField(value = "申请岗位")
    private String postName;

    @ExcelField(value = "增补人数")
    private int num;

}