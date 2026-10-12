package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReportDetailDayVo {
    private Integer num;
    //统计标识
    private String statisticalFlag;
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
    //使用时长
    private BigDecimal duration;
    //冷暖煤流量
    private BigDecimal flow;
    //单价
    private BigDecimal unitPrice;
    //合计
    private BigDecimal total;
    //环比金额
    private BigDecimal totalQOQ = BigDecimal.ZERO;
    //环比流量
    private BigDecimal flowQOQ = BigDecimal.ZERO;
    //环比时长
    private BigDecimal durationQOQ = BigDecimal.ZERO;
    //环比金额比例
    private BigDecimal totalQOQRatio = BigDecimal.ZERO;
    //环比流量比例
    private BigDecimal flowQOQRatio = BigDecimal.ZERO;
    //环比时长比例
    private BigDecimal durationQOQRatio = BigDecimal.ZERO;
    //同比金额
    private BigDecimal totalYOY = BigDecimal.ZERO;
    //同比流量
    private BigDecimal flowYOY = BigDecimal.ZERO;
    //同比时长
    private BigDecimal durationYOY = BigDecimal.ZERO;
    //同比金额比例
    private BigDecimal totalYOYRatio = BigDecimal.ZERO;
    //同比流量比例
    private BigDecimal flowYOYRatio = BigDecimal.ZERO;
    //同比时长比例
    private BigDecimal durationYOYRatio = BigDecimal.ZERO;

}
