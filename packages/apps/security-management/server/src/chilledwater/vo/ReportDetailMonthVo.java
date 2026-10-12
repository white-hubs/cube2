package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportDetailMonthVo {
    private Integer num;
    //统计标识
    private String statisticalFlag;
    //统计周期
    private String statisticalCycle;
    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //档主编号
    private String shiftMasterPhone;
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
    //使用时长
    private BigDecimal duration;
    //冷暖煤流量
    private BigDecimal flow;
    //单价
    private BigDecimal unitPrice;
    //合计
    private BigDecimal total;
}
