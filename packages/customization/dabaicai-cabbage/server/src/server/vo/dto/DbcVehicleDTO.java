package com.guhecloud.cube.srv.gen.cabbage.server.vo.dto;

import lombok.Data;

/**
 * 大白菜车辆
 */
@Data
public class DbcVehicleDTO {
    /**
     * 车牌号码
     */
    private String vehicleNumber;
    /**
     * 车辆类型编号
     */
    private String vehicleTypeCode;
    /**
     * 车辆类型名称
     */
    private String vehicleTypeName;
    /**
     * 车主姓名
     */
    private String customerName;
    /**
     * 车主联系手机号
     */
    private String customerMobile;
    //市场序号
    private Integer marketId;
    //市场名称
    private String marketName;
}
