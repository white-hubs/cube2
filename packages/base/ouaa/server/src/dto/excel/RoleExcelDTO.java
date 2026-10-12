package com.guhecloud.cube.biz.ouaa.server.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * author: ht
 * create: 2025-06-11 14:00
 */
@Data
public class RoleExcelDTO {
    @ExcelIgnore
    private Long id;
    /**
     * 角色名称
     */
    @ExcelProperty("角色名称")
    private String name;
    /**
     * 所属产品
     */
    @ExcelProperty("所属产品")
    private String product;
    /**
     * 是否启用
     */
    @ExcelIgnore
    private Boolean enabled;
    @ExcelProperty("角色状态")
    private String enableText;
    @ExcelProperty("备注")
    private String remark;
    @ExcelProperty("菜单权限")
    private String roleMenus;
    @ExcelProperty("关联人员")
    private String roleRelationUsers;
    @ExcelProperty("功能权限")
    private String roleFunctionPermissions;
    @ExcelProperty("数据权限")
    private String roleDataPermissions;

}
