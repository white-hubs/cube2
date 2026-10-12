package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.infrastructure.valueobj;

import annotation.FormField;
import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/5 17:57
 * @Description:
 */
@Data
public class IllegalParkingApprovalInfo {

    @FormField(label = "严重程度")
    private String illegalLevel;
    @FormField(label = "车牌号")
    private String vehicleNo;
    @FormField(label = "车主姓名")
    private String ownerName;
    @FormField(label = "车主电话")
    private String ownerPhone;
    @FormField(label = "车牌颜色")
    private String plateColor;
    @FormField(label = "车场编号")
    private String parkingLot;
}
