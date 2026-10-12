package com.guhecloud.cube.srv.gen.work.permit.server.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 作业类型配置导出行。
 */
@Data
public class WorkPermitTypeConfigExportVO {
    @ExcelProperty("作业类型名称")
    private String typeName;
    @ExcelProperty("作业等级")
    private String workPermitLevel;
    @ExcelProperty("等级定义")
    private String levelDefinition;
    @ExcelProperty("作业检查(项数)")
    private Integer riskCheckCount;
    @ExcelProperty("作业验收（项数）")
    private Integer acceptanceCount;
    @ExcelProperty("作业票有效时长")
    private Integer permitValidHours;
}
