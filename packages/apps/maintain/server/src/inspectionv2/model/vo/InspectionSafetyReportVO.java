package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;

@Data
public class InspectionSafetyReportVO {
    //对象类型
    private String targetCategory;
    //部门名称
    private String organizationName;
    //巡检次数
    private Long num;
    //正常次数
    private Long normalNum;
    //异常次数
    private Long abnormalNum;
}
