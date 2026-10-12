package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportDetailYearVo {

    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //档位号
    private String gearPositionCode;
    //部门
    private String departmentName;
    //区域
    private String gearPositionArea;
    //设备编码
    private String deviceCode;
    //单位流量
    private BigDecimal unitTraffic;
    private BigDecimal one;
    private BigDecimal two;
    private BigDecimal three;
    private BigDecimal four;
    private BigDecimal five;
    private BigDecimal six;
    private BigDecimal seven;
    private BigDecimal eight;
    private BigDecimal nine;
    private BigDecimal ten;
    private BigDecimal eleven;
    private BigDecimal twelve;
    //合计
    private BigDecimal total;

}
