package com.guhecloud.cube.biz.ouaa.server.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-25  10:26
 * @Description:
 * @Version: 1.0
 */
@Data
public class UserExcelVO {
    @ExcelProperty(value = "*姓名")
    private String name;
    @ExcelProperty(value = "*联系电话")
    private String mobile;
    @ExcelProperty(value = "性别")
    private String gender;
    @ExcelProperty(value = "部门")
    private String organizationName;
    @ExcelIgnore
    private Long organizationId;
    @ExcelProperty(value = "管理部门")
    private String managedOrganizationIds;
    @ExcelIgnore
    private Long managedOrganizationId;
    @ExcelProperty(value = "岗位")
    private String appointmentIds;
    @ExcelProperty(value = "职位")
    private String positionIds;
    @ExcelProperty(value = "角色")
    private String roleNames;
    @ExcelProperty(value = "身份证号")
    private String idCard;

}
