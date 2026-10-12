package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DataDeviceVo {
    private String deviceCode;
    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //档主姓名
    private String shiftMasterPhone;
    //档位号
    private String gearPositionCode;
    //部门id
    private String departmentId;
    //部门id
    private String department;
    //档位区域
    private String gearPositionAreaId;
    //档位区域
    private String gearPositionArea;
    //冷媒体单位流量
    private String unitTraffic;
    //单价
    private BigDecimal unitPrice;
    private Integer tenant;
    private Long deviceId;
    private LocalDateTime billableTime;

}
