package com.guhecloud.cube.biz.ouaa.server.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-10 19:06
 */
@Data
public class OrgExcelDTO {
    /**
     * 组织机构名称
     */
    @ExcelProperty("部门名称")
    private String name;
    /**
     * 组织机构备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 组织机构成员数量
     */
    @ExcelProperty("人数")
    private Integer members;

    /**
     * 是否启用
     */

    @ExcelIgnore
    private Boolean enabled;
    @ExcelProperty("部门状态")
    private String enableText;
}
