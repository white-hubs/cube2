package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

/**
 * @author Appet
 * @Description 应急人员管理
 * @date 2024-01-22 10:08:36
 */
@Data
public class EmergencyUserParam {

    //姓名
    private String name;
    //性别
    private String sex;
    //联系方式
    private String phone;
    //人员类型编号
    private String categoryCode;
    //人员类型名称
    private String categoryName;
    //来源组织(手动填写的组织)
    private String fromOrg;
    //专业特长描述
    private String skillDesc;
    //所属组织
    private Long emgOrgId;

}