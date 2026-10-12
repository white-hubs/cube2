package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

@Data
public class ElectricityVo {

    //变压设备
    private String transformerMeterCode;
    //变压器a相电流
    private String transformerMeterA;
    //变压器b相电流
    private String transformerMeterB;
    //变压器配电
    private String transformerCurrentQuantity;
    //配电设备
    private String powerMeterCode;
    //配电a相电流
    private String powerMeterA;
    //配电b相电流
    private String powerMeterB;
    //配电电流
    private String powerCurrentQuantity;

}
