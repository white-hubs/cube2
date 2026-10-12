package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WholesalerDeviceVo {
    private Integer num;
    //批发商编码
    private String code;
    //批发商档主
    private String shiftMaster;
    //档主电话
    private String shiftMasterPhone;
    //档位编号
    private String gearPositionCode;
    //区域
    private String gearPositionArea;
    //部门
    private String department;
    //设备编号
    private String deviceCode;
    //冷媒体单位流量
    private String unitTraffic;
    private String createBy;
    private LocalDateTime createTime;
    private String updateBy;
    private LocalDateTime updateTime;
    //商户id
    private Long wholesalerId;
}
