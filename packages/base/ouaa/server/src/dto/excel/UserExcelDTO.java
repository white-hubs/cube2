package com.guhecloud.cube.biz.ouaa.server.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-09 17:06
 */
@Data
public class UserExcelDTO {
    @ExcelIgnore
    private Long id;
    /**
     * 用户姓名
     */
    @ExcelProperty("姓名")
    private String name;

    /**
     * 联系电话
     */
    @ExcelProperty("联系电话")
    private String mobile;

    /**
     * 用户备注
     */
    @ExcelProperty("备注")
    private String remark;

    /**
     * 所属部门
     */
    @ExcelProperty("所属部门")
    private String organizations;

    /**
     * 管理的部门
     */
    @ExcelProperty("管理部门")
    private String managedOrganizations;

    @ExcelProperty("职位")
    private String appointmentList;
    /**
     * 岗位列表
     */
    @ExcelProperty("岗位")
    private String positions;

    /**
     * 角色名称列表
     */
    @ExcelProperty("角色")
    private String roles;
    @ExcelIgnore
    private Boolean enabled;
    /**
     * 是否启用
     */
    @ExcelProperty("是否启用")
    private String enableText;

}
